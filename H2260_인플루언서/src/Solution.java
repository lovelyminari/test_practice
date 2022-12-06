import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
	private final static int CMD_INIT = 1;
	private final static int CMD_INFLUENCER = 2;
	private final static int CMD_ADD_PURCHASING_POWER = 3;
	private final static int CMD_ADD_FRIENDSHIP = 4;
	
	private final static UserSolution usersolution = new UserSolution();

	private static int[] mPurchasingPower = new int[20000];
	private static int[] mFriend1 = new int[20000];
	private static int[] mFriend2 = new int[20000];

	private static boolean run(BufferedReader br) throws Exception {
		StringTokenizer st;

		int numQuery;
		int N, M, mRank, mID, mPower, mID1, mID2;
		int userAns, ans;

		boolean isCorrect = false;

		//명령줄의 갯수
		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; q++) {
			st = new StringTokenizer(br.readLine(), " ");

			//명령줄의 첫번째 글자는 명령의 종류임(1, 2, 3, 4)
			int cmd;
			cmd = Integer.parseInt(st.nextToken());

			switch(cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N; i++)
					mPurchasingPower[i] = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for (int i = 0; i < M; i++)
					mFriend1[i] = Integer.parseInt(st.nextToken());
				for (int i = 0; i < M; i++)
					mFriend2[i] = Integer.parseInt(st.nextToken());
				usersolution.init(N, mPurchasingPower, M, mFriend1, mFriend2);
				isCorrect = true;
				break;
			case CMD_INFLUENCER:
				mRank = Integer.parseInt(st.nextToken());
				userAns = usersolution.influencer(mRank);
				ans = Integer.parseInt(st.nextToken());
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			case CMD_ADD_PURCHASING_POWER:
				mID = Integer.parseInt(st.nextToken());
				mPower = Integer.parseInt(st.nextToken());
				userAns = usersolution.addPurchasingPower(mID, mPower);
				ans = Integer.parseInt(st.nextToken());
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			case CMD_ADD_FRIENDSHIP:
				mID1 = Integer.parseInt(st.nextToken());
				mID2 = Integer.parseInt(st.nextToken());
				userAns = usersolution.addFriendship(mID1, mID2);
				ans = Integer.parseInt(st.nextToken());
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			default:
				isCorrect = false;
				break;
			}
			
			if(!isCorrect) {
				System.out.println("false query = " + (q + 1) + " / cmd = " + cmd);
				break;
			}
		}
		return isCorrect;
	}

	public static void main(String[] args) throws Exception
	{
		int T, MARK;
	
		//TODO
		System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		T = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= T; tc++)
		{
			//score가 1 이상이면 100(정답), 0이면 0(오답)이 출력됨
			int score = run(br) ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}

		br.close();
	}
}