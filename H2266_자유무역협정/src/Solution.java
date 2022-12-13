import java.util.Scanner;

class Solution
{
	private static final int MAXN	= 500;
	private static final int MAXL	= 11;
	
	private static final int CMD_INIT 				= 100;
	private static final int CMD_START_PROJECT 		= 200;
	private static final int CMD_FINISH_PROJECT		= 300;
	private static final int CMD_ALLY				= 400;
	private static final int CMD_CONFLICT 			= 500;
	
	private static int mstrcmp(char[] a, char[] b)
	{
		int idx = 0;
		while (a[idx] != '\0' && a[idx] == b[idx])
			++idx;
		return a[idx] - b[idx];
	}

	private static void String2Char(String s, char[] b)
	{
        int n = s.length();
        for (int i = 0; i < n; ++i)
            b[i] = s.charAt(i);
        for (int i = n; i < MAXL; ++i)
        	b[i] = '\0';
    }
	
    private static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception 
    {
    	int Q;
    	int mNumA, mNumB;
    	
    	char[][] mCompanyListA = new char[MAXN][MAXL];
    	char[][] mCompanyListB = new char[MAXN][MAXL];
    	
    	char[] mCompanyA = new char[MAXL];
    	char[] mCompanyB = new char[MAXL];
    	char[] mCompany1 = new char[MAXL];
    	char[] mCompany2 = new char[MAXL];
    	
    	int ret = -1, ans;

    	Q = sc.nextInt();
    	
    	boolean okay = false;
    	
    	for (int q = 0; q < Q; ++q)
    	{
    		int cmd = sc.nextInt();

            switch(cmd)
            {
            case CMD_INIT:
            	mNumA = sc.nextInt();
            	for (int i = 0; i < mNumA; ++i)
            		String2Char(sc.next(), mCompanyListA[i]);
            	mNumB = sc.nextInt();
            	for (int i = 0; i < mNumB; ++i)
            		String2Char(sc.next(), mCompanyListB[i]);
            	usersolution.init(mNumA, mCompanyListA, mNumB, mCompanyListB);
            	okay = true;
            	break;
            case CMD_START_PROJECT:
            	String2Char(sc.next(), mCompanyA);
            	String2Char(sc.next(), mCompanyB);
            	usersolution.startProject(mCompanyA, mCompanyB);
            	break;
            case CMD_FINISH_PROJECT:
            	String2Char(sc.next(), mCompanyA);
            	String2Char(sc.next(), mCompanyB);
            	usersolution.finishProject(mCompanyA, mCompanyB);
            	break;
            case CMD_ALLY:
            	String2Char(sc.next(), mCompany1);
            	String2Char(sc.next(), mCompany2);
            	usersolution.ally(mCompany1, mCompany2);
            	break;            	
            case CMD_CONFLICT:
            	String2Char(sc.next(), mCompany1);
            	String2Char(sc.next(), mCompany2);
            	ret = usersolution.conflict(mCompany1, mCompany2);
            	ans = sc.nextInt();
            	if (ans != ret)
            		okay = false;
            	break;    	
            default:
            	okay = false;
            	break;
            }
            
    	}
 
    	return okay;
    }
    
    public static void main(String[] args) throws Exception
    {
        System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		Scanner sc = new Scanner(System.in);
		int TC = sc.nextInt();
        int MARK = sc.nextInt();
        
        for (int testcase = 1; testcase <= TC; ++testcase)
        {
			int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();
    }
}