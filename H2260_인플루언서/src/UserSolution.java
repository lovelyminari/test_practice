import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class Influencer {
	int id;					//id
	int purchasingPower;	//구매력
	int effectivePower;
	ArrayList<Influencer> friendships;
	
	public Influencer(int id, int purchasingPower) {
		this.id = id;
		this.purchasingPower = purchasingPower;
		effectivePower = 0;
		friendships = new ArrayList<>();
	}
	
	public void addFriend(Influencer friend) {
		friendships.add(friend);
	}
	
	public void addPurchasingPower(int power) {
		this.purchasingPower += power;
	}
	
	public void calculateEffectivePower() {
		int power_0 = this.purchasingPower;	//0촌의 구매력의 합
		int power_1 = 0;					//1촌의 구매력의 합
		int power_2 = 0;					//2촌의 구매력의 합
		int power_3 = 0;					//3촌의 구매력의 합
		Set<Integer> friendSet = new HashSet<>();
		
		friendSet.add(this.id);
		
		if(!this.friendships.isEmpty()) {
			//1촌 계산
			for(Influencer chon_1 : this.friendships) {
				if(!friendSet.contains(chon_1.id)) {
					friendSet.add(chon_1.id);
					power_1 += chon_1.purchasingPower;
				}
			}
			
			//2촌 계산
			for(Influencer chon_1 : this.friendships) {
				for(Influencer chon_2 : chon_1.friendships) {
					if(!friendSet.contains(chon_2.id)) {
						friendSet.add(chon_2.id);
						power_2 += chon_2.purchasingPower;
					}
				}
			}
			
			//3촌 계산
			for(Influencer chon_1 : this.friendships) {
				for(Influencer chon_2 : chon_1.friendships) {
					for(Influencer chon_3 : chon_2.friendships) {
						if(!friendSet.contains(chon_3.id)) {
							friendSet.add(chon_3.id);
							power_3 += chon_3.purchasingPower;
						}
					}
				}
			}
		}
		
		//영향력 = 0촌 구매력*10 + 1촌 구매력 총합*5 + 2촌 구매력 총합*3 + 3촌 구매력 총합*2
		this.effectivePower = (power_0 * 10) + (power_1 * 5) + (power_2 * 3) + (power_3 * 2);
	}

	@Override
	public String toString() {
		return "Influencer [id=" + id + ", purchasingPower=" + purchasingPower + ", effectivePower=" + effectivePower
				+ ", friendships={" + printFriendship() + "}]";
	}
	
	public String printFriendship() {
		String str = "";
		for(Influencer friend : friendships) {
			str += friend.id + " ";
		}
		return str;
	}
	
}

class UserSolution {
	int N;
	int[] mPurchasingPower;
	ArrayList<Influencer> influencers;		//각 인덱스마다 Influencer 객체 한개씩 저장
	TreeSet<Influencer> sortedInfluencer;	//인플루언서 정렬 by 영향력
	
	public void init(int N, int mPurchasingPower[], int M, int mFriend1[], int mFriend2[]) {
		this.N = N;
		this.mPurchasingPower = mPurchasingPower;
		influencers = new ArrayList<>();
		sortedInfluencer = new TreeSet<>(new Comparator<Influencer>() {

			@Override
			public int compare(Influencer o1, Influencer o2) {
				//-1, 0, 1 as the first argument is less than, equal to, greater than the second.
				//내림차순
				if(o1.effectivePower > o2.effectivePower) {
					return -1;
				}
				else if(o1.effectivePower == o2.effectivePower)
				{
					//id가 작은 것이 앞에 옴
					if(o1.id > o2.id) return 1;
					else if(o1.id == o2.id) return 0;
					else return -1;
				}
				else {
					return 1;
				}
			}
			
		});

		/* 인플루언스 리스트 생성 */
		for (int i = 0; i < N; i++) {
			Influencer influencer = new Influencer(i, mPurchasingPower[i]);
			influencers.add(influencer);
		}
		
		/* 초기 친구관계 리스트 생성 */
		for (int i = 0; i < M; i++) {
			Influencer friend1 = influencers.get(mFriend1[i]);
			Influencer friend2 = influencers.get(mFriend2[i]);
			friend1.addFriend(friend2);
			friend2.addFriend(friend1);
		}

		/* 최종 영향력 계산 */
		for(Influencer influencer : influencers) {
			influencer.calculateEffectivePower();
			/* 정렬 */
			sortedInfluencer.add(influencer);
		}
		
	}
	
	public void printSortedList() {
		for(Influencer o : sortedInfluencer)  System.out.println(o);
	}

	public int influencer(int mRank) {
		int idx = 0;
		for(Influencer o : sortedInfluencer) {
			if(idx == mRank-1) {
				return o.id;
			}
			idx++;
		}
		return -1;
	}

	public int addPurchasingPower(int mID, int mPower) {
		Influencer o = influencers.get(mID);
		o.addPurchasingPower(mPower);
		
		//0촌, 1촌, 2촌, 3촌의 영향력 재계산, 재정렬
		updateSortedChonPower(o);
		
		return o.effectivePower;
	}

	public int addFriendship(int mID1, int mID2) {
		Influencer o1 = influencers.get(mID1);
		Influencer o2 = influencers.get(mID2);
		
		//친구관계 만들기
		o1.addFriend(o2);
		o2.addFriend(o1);
		
		//o1, o2의 친구관계 변화로 인해 친구들의 영향력 재계산
		//양방향 그래프이므로 0촌, 1, 2, 3촌만 재계산, 재정렬
		updateSortedChonPower(o1);
		updateSortedChonPower(o2);
		
		return o1.effectivePower + o2.effectivePower;
	}
	
	public void updateSortedChonPower(Influencer o) {
		Set<Integer> friendSet = new HashSet<>();
		friendSet.add(o.id);
		
		//o의 구매력 변화로 인해 친구들의 영향력 재계산
		//양방향 그래프이므로 o의 1, 2, 3촌만 재계산
		for(Influencer chon_1 : o.friendships) {
			friendSet.add(chon_1.id);
			for(Influencer chon_2 : chon_1.friendships) {
				friendSet.add(chon_2.id);
				for(Influencer chon_3 : chon_2.friendships) {
					friendSet.add(chon_3.id);
				}
			}
		}
		
		for(int friendIdx : friendSet) {
			Influencer i = influencers.get(friendIdx);
			sortedInfluencer.remove(i);
			i.calculateEffectivePower();
			sortedInfluencer.add(i);
		}
		
	}
	
}