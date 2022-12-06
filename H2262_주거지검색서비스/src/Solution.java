import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_FIND = 300;

    private final static int MAX_N = 3000;
    private static int N;
    private static int M;
    private static int mDownTown[] = new int[3];
    private static int city_id_list[] = new int[MAX_N + 5];
    private static int distance_list[] = new int[MAX_N + 5];
    private static int downtown_list[] = new int[4];

    private static boolean run() throws Exception {

        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

        int query_num = Integer.parseInt(stdin.nextToken());
        int ret, ans;
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            stdin = new StringTokenizer(br.readLine(), " ");
            int query = Integer.parseInt(stdin.nextToken());

            if (query == CMD_INIT) {
            	N = Integer.parseInt(stdin.nextToken());
                for (int i = 0; i < 3; i++)
                {
                	mDownTown[i] = Integer.parseInt(stdin.nextToken());
                }
                userSolution.init(N, mDownTown);
                ok = true;
            } else if (query == CMD_ADD) {
                int mLimitDistance;

                M = Integer.parseInt(stdin.nextToken());
                mLimitDistance = Integer.parseInt(stdin.nextToken());

                for (int i = 0; i < M; i++)
                {
                	city_id_list[i] = Integer.parseInt(stdin.nextToken());;
                }
                for (int i = 0; i < M - 1; i++)
                {
                	distance_list[i] = Integer.parseInt(stdin.nextToken());;
                }
                userSolution.newLine(M, city_id_list, distance_list);
                userSolution.changeLimitDistance(mLimitDistance);
            } else if (query == CMD_FIND) {
                int mOpt = Integer.parseInt(stdin.nextToken());

                for (int i = 0; i < mOpt; i++)
                {
                	downtown_list[i] = Integer.parseInt(stdin.nextToken());
                }
                ret = userSolution.findCity(mOpt, downtown_list);
                ans = Integer.parseInt(stdin.nextToken());

                if (ans != ret)
                {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("res/test_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}
