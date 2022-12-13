import java.util.HashMap;
 
class UserSolution_ans
{
     class Company {
        int ally = 1;
        Company parent = null;
        //���� ��� ����
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
           
        // ���� �δ� ���̹Ƿ� ��� �պ�~
        // ���� ��� �� �÷��ְ�
        A.ally += B.ally;
        // �������� �պ�
        B.parent = A;
            
        // ����Ǵ� ���(B)�� ���� ��Ͽ� A�� �ݿ�
        for (Company company : B.mapProject.keySet()) {
               
            // �̹� �ش� ����� A ����� ������ �� ������
            // �̸� ���� ��� �� �����س���
            int x = B.mapProject.get(company);
               
            // �ߺ� ����
            company.mapProject.remove(B);
            // ���� ���ٸ� �̸� ���ؼ� ���� X �� �ְ� �ƴϸ� X ��ŭ �߰�
            company.mapProject.compute(A, (k, v) -> v == null ? x : v + x);
               
            A.mapProject.compute(company, (k, v) -> v == null ? x : v + x);
        }
    }
    
    public int conflict(char[] mCompany1, char[] mCompany2) {
        Company A = map.get(new String(mCompany1)).getParent();
        Company B = map.get(new String(mCompany2)).getParent();
           
        int count = 0;
           
        // A ����� ���� ����� ����� ���鼭
                  
        for (Company company : A.mapProject.keySet()) {
            if (B.mapProject.containsKey(company)) {
                count += company.ally;
            }
        }
        return count;
    }
     
}