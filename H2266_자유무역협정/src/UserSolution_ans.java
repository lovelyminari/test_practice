import java.util.HashMap;
 
class UserSolution_ans
{
     class Company {
        int ally = 1;
        Company parent = null;
        //협업 목록 저장
        HashMap<Company, Integer> mapProject = new HashMap<>();
    
        Company getParent() {
            if (parent == null)
                return this;
            return parent = parent.getParent();
        }
    }
    
    HashMap<String, Company> map = new HashMap<>();
    
    public void init(int mNumA, char[][] mCompanyListA, int mNumB, char[][] mCompanyListB) {
        map.clear();
    
        for (int i = 0; i < mNumA; i++) {
            map.put(new String(mCompanyListA[i]), new Company());
        }
    
        for (int i = 0; i < mNumB; i++) {
            map.put(new String(mCompanyListB[i]), new Company());
        }
    }
    
    public void startProject(char[] mCompanyA, char[] mCompanyB) {
        Company A = map.get(new String(mCompanyA)).getParent();
        Company B = map.get(new String(mCompanyB)).getParent();
    
        A.mapProject.compute(B, (k, v) -> v == null ? 1 : v + 1);
        B.mapProject.compute(A, (k, v) -> v == null ? 1 : v + 1);
    }
    
    public void finishProject(char[] mCompanyA, char[] mCompanyB) {
        Company A = map.get(new String(mCompanyA)).getParent();
        Company B = map.get(new String(mCompanyB)).getParent();
    
        A.mapProject.compute(B, (k, v) -> v == 1 ? null : v - 1);
        B.mapProject.compute(A, (k, v) -> v == 1 ? null : v - 1);
    }
    
    public void ally(char[] mCompany1, char[] mCompany2) {
        Company A = map.get(new String(mCompany1)).getParent();
        Company B = map.get(new String(mCompany2)).getParent();
    
        if(A == B) return;
           
        // 동맹 맺는 것이므로 흡수 합병~
        // 동맹 기업 수 늘려주고
        A.ally += B.ally;
        // 한쪽으로 합병
        B.parent = A;
            
        // 흡수되는 기업(B)의 협업 목록에 A로 반영
        for (Company company : B.mapProject.keySet()) {
               
            // 이미 해당 기업에 A 기업이 존재할 수 있으니
            // 미리 협업 기업 수 저장해놓고
            int x = B.mapProject.get(company);
               
            // 중복 제거
            company.mapProject.remove(B);
            // 만약 없다면 미리 위해서 구한 X 값 넣고 아니면 X 만큼 추가
            company.mapProject.compute(A, (k, v) -> v == null ? x : v + x);
               
            A.mapProject.compute(company, (k, v) -> v == null ? x : v + x);
        }
    }
    
    public int conflict(char[] mCompany1, char[] mCompany2) {
        Company A = map.get(new String(mCompany1)).getParent();
        Company B = map.get(new String(mCompany2)).getParent();
           
        int count = 0;
           
        // A 기업의 협업 기업들 목록을 돌면서
                  
        for (Company company : A.mapProject.keySet()) {
            if (B.mapProject.containsKey(company)) {
                count += company.ally;
            }
        }
        return count;
    }
     
}