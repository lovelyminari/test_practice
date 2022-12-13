import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
 

   
class UserSolution_ans2 {
    List<String> countryA;
    List<String> countryB;
    List<Company> companyA;
    List<Company> companyB;
    
    static class Company {
        int id;
        HashSet<Integer> ally = new HashSet<>();
        HashSet<Integer> project = new HashSet<>();
       
        Company(int id) {
            this.id = id;
        }
    }
   
    public void init(int mNumA, char[][] mCompanyListA, int mNumB, char[][] mCompanyListB) {
        countryA = new ArrayList<>();
        countryB = new ArrayList<>();
        companyA = new ArrayList<>();
        companyB = new ArrayList<>();
   
        for (int i = 0; i < mNumA; ++i) {
            countryA.add(new String(mCompanyListA[i]));
            companyA.add(new Company(i));
        }
   
        for (int i = 0; i < mNumB; ++i) {
            countryB.add(new String(mCompanyListB[i]));
            companyB.add(new Company(i));
        }
    }
   
    public void startProject(char[] mCompanyA, char[] mCompanyB) {
        int idCompanyA = countryA.indexOf(new String(mCompanyA));
        int idCompanyB = countryB.indexOf(new String(mCompanyB));
   
        companyA.get(idCompanyA).project.add(idCompanyB);
        companyB.get(idCompanyB).project.add(idCompanyA);
    }
   
    public void finishProject(char[] mCompanyA, char[] mCompanyB) {
        int idCompanyA = countryA.indexOf(new String(mCompanyA));
        int idCompanyB = countryB.indexOf(new String(mCompanyB));
   
        companyA.get(idCompanyA).project.remove(idCompanyB);
        companyB.get(idCompanyB).project.remove(idCompanyA);
    }
   
    public void ally(char[] mCompany1, char[] mCompany2) {
        String company1 = new String(mCompany1);
        String company2 = new String(mCompany2);
   
        List<String> country;
        List<Company> company;
         
        if (countryA.contains(company1)) {
            country = countryA;
            company = companyA;
        } else {
            country = countryB;
            company = companyB;
        }
   
        int idCompany1 = country.indexOf(company1);
        int idCompany2 = country.indexOf(company2);
   
        company.get(idCompany1).ally.add(idCompany2);
        company.get(idCompany2).ally.add(idCompany1);
    }
   
    private HashSet<Integer> getProjectAllies(String companyName) {
        List<String> country;
        List<Company> company;
        List<Company> overseasCompany;
         
        if (countryA.contains(companyName)) {
            country = countryA;
            company = companyA;
            overseasCompany = companyB;
        } else {
            country = countryB;
            company = companyB;
            overseasCompany = companyA;
        }
        int companyId = country.indexOf(companyName);
   
        List<Integer> allyList = new ArrayList<>();
        HashSet<Integer> resultSet = new HashSet<>();        
        int checked = 1;        
        allyList.add(companyId);
        allyList.addAll(company.get(companyId).ally);
        resultSet.addAll(allyList);
   
        do {
            for (int i = checked; i < allyList.size(); ++i) {
                Integer cId = allyList.get(i);
                ++checked;
                Iterator<Integer> it = company.get(cId).ally.iterator();
                while (it.hasNext()) {
                    Integer allyId = it.next();
                    if (!resultSet.contains(allyId)) {
                        allyList.add(allyId);
                        resultSet.add(allyId);
                    }
                }
            }
        } while (checked != resultSet.size());
   
        resultSet.clear();
        for (int id : allyList) {
            resultSet.addAll(company.get(id).project);
        }
        allyList.clear();
        allyList.addAll(resultSet);
   
        checked = 0;
        do {
            for (int i = checked; i < allyList.size(); ++i) {
                Integer cId = allyList.get(i);
                ++checked;
                Iterator<Integer> it = overseasCompany.get(cId).ally.iterator();
                while (it.hasNext()) {
                    Integer allyId = it.next();
                    if (!resultSet.contains(allyId)) {
                        allyList.add(allyId);
                        resultSet.add(allyId);
                    }
                }
            }
        } while (checked != resultSet.size());
   
        return resultSet;
    }
   
    public int conflict(char[] mCompany1, char[] mCompany2) {
        String company1 = new String(mCompany1);
        String company2 = new String(mCompany2);
   
        HashSet<Integer> setCompany1 = getProjectAllies(company1);
        HashSet<Integer> setCompany2 = getProjectAllies(company2);
   
        setCompany1.retainAll(setCompany2);
        return setCompany1.size();
    }
}