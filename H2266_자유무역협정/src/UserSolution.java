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
		
//		System.out.println(name1 + " ���� �� = " + c1);
//		System.out.println(name2 + " ���� �� = " + c2);
		
		//��� 1�� ��� 2�� �����̰� ��� 3�� ��� 4�� ������ �� ��� 1�� ��� 3�� ������ ������ ��� 2�� ��� 3�� ������ �ǰ� ��� 2�� ��� 4�� ������ �ȴ�.
		//��� mCompany1�� ��� mCompany2�� �̹� ������ ���� �ִ�. (�ٸ� ����� ������ �ξ �̹� ������ �Ǿ��� ���� �ִ�)
		//�ϴ� ������ ���͵��� �� �����ؼ� ������ �����
		HashSet<String> allyNm = (HashSet<String>) c1.allies.keySet().stream().collect(Collectors.toSet());
		allyNm.addAll(c2.allies.keySet());
		allyNm.add(name1);
		allyNm.add(name2);
		
		//c1+���ͱ���� c2+���ͱ���� ���� ��� sync ó��
		for(String name : allyNm) {
			Company ally = companyMap.get(name);
			
			ally.allies.putAll(c1.allies);
			ally.allies.putAll(c2.allies);
			
			ally.allies.put(name1, c1);
			ally.allies.put(name2, c2);
		}
		
		//�ڱ� �ڽ��� �����
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
		
		//1. ��� 1 �Ǵ� ��� 1�� ������ ����� ���� ������Ʈ�� �ϰ� �ִ� ��� �Ǵ� �� ����� ������ ���
		HashSet<String> set1 = findSolver(c1);
//		System.out.println(name1 + "�� �������� �ĺ� : " + set1);
		//2. ��� 2 �Ǵ� ��� 2�� ������ ����� ���� ������Ʈ�� �ϰ� �ִ� ��� �Ǵ� �� ����� ������ ���
		HashSet<String> set2 = findSolver(c2);
//		System.out.println(name2 + "�� �������� �ĺ� : " + set2);
		
		set1.retainAll(set2);
		
		int cnt = 0;
		for(String key : set1) {
			Company c = companyMap.get(key);
			if(!c1.country.equals(c.country)) {
				cnt++;
			}
		}
		
//		System.out.println("�������� ȸ�� ���� : " + cnt);
		
		return cnt;
	}
	
	public HashSet<String> findSolver(Company c) {
		HashSet<String> rsltSet = new HashSet<>();
		
		//��� 1�� ���� ������Ʈ�� �ϰ� �ִ� ���
		rsltSet.addAll(c.projects.keySet());
//		System.out.println(c.name + "�� ������Ʈ ���� ��� : " + c.projects.keySet());
//		System.out.println("rsltSet : " + rsltSet);
		
		//�Ǵ� ��� 1�� ���� ������Ʈ�� �ϰ� �ִ� ����� ���� ���
		for(String key : c.projects.keySet()) {
			Company projectCom = c.projects.get(key);
//			System.out.println(projectCom);
			rsltSet.addAll(projectCom.allies.keySet());
		}
		
		//�Ǵ� ��� 1�� ���� ����� ���� ������Ʈ�� �ϰ� �ִ� ���
		for(String key : c.allies.keySet()) {
			Company ally = c.allies.get(key);
			rsltSet.addAll(ally.projects.keySet());
			
			//�Ǵ� ��� 1�� ���� ����� ���� ������Ʈ�� �ϰ� �ִ� ����� ���� ���
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