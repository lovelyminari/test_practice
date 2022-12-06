import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CorrectSolution {

	HashMap<Integer,ArrayList<Integer>> friendList;
	int purchasingPower[];
	ArrayList<Influencer2> influencerTree;
	TreeSet<Influencer2> sortByPower;
	int total= 0;

	public void init(int N, int mPurchasingPower[], int M, int mFriend1[], int mFriend2[])
	{
		friendList = new HashMap<>();
		sortByPower = new TreeSet<Influencer2>(new Comparator<Influencer2>() {

			@Override
			public int compare(Influencer2 o1, Influencer2 o2) {

				if(o1.purchasingPower > o2.purchasingPower)
					return -1;
				else if(o1.purchasingPower == o2.purchasingPower)
				{
					if(o1.id > o2.id)
						return 1;
					else if(o1.id == o2.id) return 0;
					else return -1;
				}
				else
					return 1;
			}
		});
		influencerTree = new ArrayList<>(N);
		purchasingPower = new int[N];
		total =N;

		for(int idx =0 ;idx <M;idx++)
		{
			int friend1 = mFriend1[idx];
			int friend2 = mFriend2[idx];
			if(!friendList.containsKey(friend1))
			{
				ArrayList<Integer> list = new ArrayList<>();
				list.add(friend2);
				friendList.put(friend1, list);
			}
			else {
				friendList.get(friend1).add(friend2);
			}

			if(!friendList.containsKey(friend2))
			{
				ArrayList<Integer> list = new ArrayList<>();
				list.add(friend1);
				friendList.put(friend2, list);
			}
			else {
				friendList.get(friend2).add(friend1);
			}


			purchasingPower[idx] = mPurchasingPower[idx];
		}
		for(int idx = M-1; idx< N ; idx++)
		{
			purchasingPower[idx] = mPurchasingPower[idx];
		}

		for(int idx =0; idx<N; idx++)
		{
			int power = calcPurchasingPower(idx);
			influencerTree.add(idx,new Influencer2(idx, power));
			sortByPower.add(influencerTree.get(idx));
		}

	}
	
	public int influencer(int mRank)
	{
		int rank_idx = 0;
		for(Influencer2 i : sortByPower)
		{
			if(rank_idx == mRank-1)
			{
				return i.id;
			}
			rank_idx++;
		}
		return -1;

	}

	public int addPurchasingPower(int mID, int mPower)
	{
		purchasingPower[mID] +=mPower;
		Set<Integer> mId_friendList = new HashSet<>();
		mId_friendList.add(mID);
		int power = calcPurchasingPower(mID);
		if(friendList.get(mID)==null);
		else
		{
			for(int idx_1: friendList.get(mID)){
				mId_friendList.add(idx_1);
				for(int idx_2: friendList.get(idx_1))
				{
					mId_friendList.add(idx_2);
					for(int idx_3: friendList.get(idx_2))
					{
						mId_friendList.add(idx_3);
					}
				}
			}
		}

		for(int friend: mId_friendList)
		{
			sortByPower.remove(influencerTree.get(friend));
			influencerTree.remove(friend);
			int new_power = calcPurchasingPower(friend);
			influencerTree.add(friend, new Influencer2(friend, new_power));
			sortByPower.add(influencerTree.get(friend));
		}

		return power;
	}

	public void updateInfluencerTree(int mID)
	{
		Set<Integer> mId_friendList = new HashSet<>();

		mId_friendList.add(mID);

		for(int idx_1: friendList.get(mID))
		{
			mId_friendList.add(idx_1);
			for(int idx_2: friendList.get(idx_1))
			{
				mId_friendList.add(idx_2);
				for(int idx_3: friendList.get(idx_2))
				{
					mId_friendList.add(idx_3);
				}
			}
		}

		for(int friend: mId_friendList)
		{
			sortByPower.remove(influencerTree.get(friend));
			influencerTree.remove(friend);
			int new_power = calcPurchasingPower(friend);
			influencerTree.add(friend,new Influencer2(friend, new_power));
			sortByPower.add(influencerTree.get(friend));
		}

		return;
	}
	
	public int addFriendship(int mID1, int mID2)
	{
		if(friendList.containsKey(mID1))
		{
			friendList.get(mID1).add(mID2);
		}
		else {
			ArrayList<Integer> list = new ArrayList<>();
			list.add(mID2);
			friendList.put(mID1, list);
		}

		if(friendList.containsKey(mID2))
		{
			friendList.get(mID2).add(mID1);
		}
		else {
			ArrayList<Integer> list = new ArrayList<>();
			list.add(mID1);
			friendList.put(mID2, list);
		}

		int power1 = calcPurchasingPower(mID1);
		int power2 = calcPurchasingPower(mID2);

		updateInfluencerTree(mID1);
		updateInfluencerTree(mID2);

		return power1+power2;
	}


	public int calcPurchasingPower(int idx)
	{
		int power = 0;
		int power_1 = 0;
		int power_2 = 0;
		int power_3 = 0;

		Set<Integer> addFriend = new HashSet<>(); 
		addFriend.add(idx);
		power = purchasingPower[idx];

		if(friendList.get(idx) == null)
		{

		}
		else {
			for(int friend_1 : friendList.get(idx))
			{
				if(!addFriend.contains(friend_1)) {
					power_1 += purchasingPower[friend_1];
					addFriend.add(friend_1);
				}

			}
			for(int friend_1 : friendList.get(idx) ) {
				for(int friend_2: friendList.get(friend_1))
				{
					if(!addFriend.contains(friend_2)) {
						power_2 += purchasingPower[friend_2];
						addFriend.add(friend_2);
					}
				}
			}

			for(int friend_1 : friendList.get(idx) ) {
				for(int friend_2: friendList.get(friend_1)){
					for(int friend_3 : friendList.get(friend_2)) {
						if(!addFriend.contains(friend_3)) {
							power_3 += purchasingPower[friend_3];
							addFriend.add(friend_3);
						}
					}   
				}
			}
		}

		power = power*10 +power_1*5 +power_2*3 +power_3*2;

		return power;

	}
}

class InfluenceComparator<T> implements Comparator<T>
{

	@Override
	public int compare(T o1, T o2) {
		// TODO Auto-generated method stub

		return 0;
	}

}

class Influencer2 
{
	int id;
	int purchasingPower = 0;
	
	public Influencer2(int id, int purchasingPower) {
		super();
		this.id = id;
		this.purchasingPower = purchasingPower;
	}
}