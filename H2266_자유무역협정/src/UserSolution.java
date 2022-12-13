import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

class UserSolution
{
	int mNumA, mNumB;
	HashMap<String, Company> companyMap;
	
	public void init(int mNumA, char[][] mCompanyListA, int mNumB, char[][] mCompanyListB)
	{
		this.mNumA = mNumA;
		this.mNumB = mNumB;
		
		companyMap = new HashMap<>();
		
		for(int i = 0; i < mNumA; i++) {
			String name = new String(mCompanyListA[i]);
			Company company = new Company(name, "A");
			companyMap.put(name, company);
		}
		
		for(int i = 0; i < mNumB; i++) {
			String name = new String(mCompanyListB[i]);
			Company company = new Company(name, "B");
			companyMap.put(name, company);
		}
	}
	
	public void startProject(char[] mCompanyA, char[] mCompanyB)
	{
		String nameA = new String(mCompanyA);
		Company A = companyMap.get(nameA);
		
		String nameB = new String(mCompanyB);
		Company B = companyMap.get(nameB);
		
		A.projects.put(nameB, B);
		B.projects.put(nameA, A);
	}
	
	public void finishProject(char[] mCompanyA, char[] mCompanyB)
	{
		String nameA = new String(mCompanyA);
		Company A = companyMap.get(nameA);
		
		String nameB = new String(mCompanyB);
		Company B = companyMap.get(nameB);
		
		A.projects.remove(nameB);
		B.projects.remove(nameA);
	}
	
	public void ally(char[] mCompany1, char[] mCompany2)
	{
//		System.out.println(">> ally!!!");
		String name1 = new String(mCompany1);
		Company c1 = companyMap.get(name1);

		String name2 = new String(mCompany2);
		Company c2 = companyMap.get(name2);
		
//		System.out.println(name1 + " 동맹 전 = " + c1);
//		System.out.println(name2 + " 동맹 전 = " + c2);
		
		//기업 1과 기업 2가 동맹이고 기업 3과 기업 4가 동맹일 때 기업 1과 기업 3이 동맹을 맺으면 기업 2와 기업 3도 동맹이 되고 기업 2와 기업 4도 동맹이 된다.
		//기업 mCompany1과 기업 mCompany2가 이미 동맹일 수도 있다. (다른 기업과 동맹을 맺어서 이미 동맹이 되었을 수도 있다)
		//일단 서로의 동맹들을 다 복사해서 집합을 만들고
		HashSet<String> allyNm = (HashSet<String>) c1.allies.keySet().stream().collect(Collectors.toSet());
		allyNm.addAll(c2.allies.keySet());
		allyNm.add(name1);
		allyNm.add(name2);
		
		//c1+동맹기업과 c2+동맹기업의 동맹 기업 sync 처리
		for(String name : allyNm) {
			Company ally = companyMap.get(name);
			
			ally.allies.putAll(c1.allies);
			ally.allies.putAll(c2.allies);
			
			ally.allies.put(name1, c1);
			ally.allies.put(name2, c2);
		}
		
		//자기 자신은 지운다
		c1.allies.remove(name1);
		c2.allies.remove(name2);

		
//		System.out.println(c1);
//		System.out.println(c2);
	}
	
	public int conflict(char[] mCompany1, char[] mCompany2)
	{
//		System.out.println(">> conflict!!!");
//		System.out.println(companyMap);
		String name1 = new String(mCompany1);
		String name2 = new String(mCompany2);
		
		Company c1 = companyMap.get(name1);
		Company c2 = companyMap.get(name2);
		
		//1. 기업 1 또는 기업 1의 동맹인 기업과 공동 프로젝트를 하고 있는 기업 또는 그 기업과 동맹인 기업
		HashSet<String> set1 = findSolver(c1);
//		System.out.println(name1 + "의 분쟁조정 후보 : " + set1);
		//2. 기업 2 또는 기업 2의 동맹인 기업과 공동 프로젝트를 하고 있는 기업 또는 그 기업과 동맹인 기업
		HashSet<String> set2 = findSolver(c2);
//		System.out.println(name2 + "의 분쟁조정 후보 : " + set2);
		
		set1.retainAll(set2);
		
		int cnt = 0;
		for(String key : set1) {
			Company c = companyMap.get(key);
			if(!c1.country.equals(c.country)) {
				cnt++;
			}
		}
		
//		System.out.println("분쟁조정 회사 갯수 : " + cnt);
		
		return cnt;
	}
	
	public HashSet<String> findSolver(Company c) {
		HashSet<String> rsltSet = new HashSet<>();
		
		//기업 1과 공동 프로젝트를 하고 있는 기업
		rsltSet.addAll(c.projects.keySet());
//		System.out.println(c.name + "과 프로젝트 중인 기업 : " + c.projects.keySet());
//		System.out.println("rsltSet : " + rsltSet);
		
		//또는 기업 1과 공동 프로젝트를 하고 있는 기업의 동맹 기업
		for(String key : c.projects.keySet()) {
			Company projectCom = c.projects.get(key);
//			System.out.println(projectCom);
			rsltSet.addAll(projectCom.allies.keySet());
		}
		
		//또는 기업 1의 동맹 기업과 공동 프로젝트를 하고 있는 기업
		for(String key : c.allies.keySet()) {
			Company ally = c.allies.get(key);
			rsltSet.addAll(ally.projects.keySet());
			
			//또는 기업 1의 동맹 기업과 공동 프로젝트를 하고 있는 기업의 동맹 기업
			for(String k : ally.projects.keySet()) {
				Company allPrjCom = ally.projects.get(k);
				rsltSet.addAll(allPrjCom.allies.keySet());
			}
		}
		
		return rsltSet;
	}
	
}

class Company {
	String name;
	String country;
	HashMap<String, Company> projects;
	HashMap<String, Company> allies;
	
	Company(String name, String country) {
		this.name = name;
		this.country = country;
		this.projects = new HashMap<>();
		this.allies = new HashMap<>();
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", country=" + country + ", projects=" + projects.keySet() + ", allies=" + allies.keySet() + "]";
	}
	
}