import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

class Station {
	int id;
	boolean downtown;
	int price;
	ArrayList<Integer> distances;
	ArrayList<Station> next;
	
	public Station(int id) {
		this.id = id;
		this.price = 0;
		this.downtown = false;
		distances = new ArrayList<>();
		next = new ArrayList<>();
	}
	
	public void addNext(Station s, int distance) {
		next.add(s);
		distances.add(distance);
	}
	
	public void setDowntown() {
		this.downtown = true;
	}
	
	public void addPrice() {
		this.price++;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", downtown=" + downtown + ", price=" + price + ", distances=" + distances
				+ ", next={" + nextListToString() + "}]";
	}
	
	private String nextListToString() {
		String str = "";
		
		for(Station s : next) {
			str += s.id + " ";
		}
		
		return str;
	}
	
}


class UserSolution {
	int N;
	ArrayList<Station> stations;
	int limitDistance;
	Map<Integer, Integer> candidates;
	
	void init(int N, int mDownTown[]) {
		this.N = N;
		stations = new ArrayList<>();
		
		for(int i = 1; i <= N; i++) {
			Station s = new Station(i);
			stations.add(s);
		}
		
		for(int i = 0; i < mDownTown.length; i ++) {
			stations.get(mDownTown[i]-1).setDowntown();
		}
	}
	
	void newLine(int M, int mCityIDs[], int mDistances[]) {
		for(int i = 0; i < M-1; i ++) {
			Station s1 = stations.get(mCityIDs[i]-1);
			Station s2 = stations.get(mCityIDs[i+1]-1);
			
			if(!s1.next.contains(s2)) {
				s1.addNext(s2, mDistances[i]);
				s2.addNext(s1, mDistances[i]);
			} else {
				int idx1 = s1.next.indexOf(s2);
				int idx2 = s2.next.indexOf(s1);
				if(s1.distances.get(idx1) > mDistances[i]) {
					s1.distances.set(idx1, mDistances[i]);
					s2.distances.set(idx2, mDistances[i]);
				}
			}
		}
		
		printStations();
		
	}
	
	void changeLimitDistance(int mLimitDistance)
	{
		this.limitDistance = mLimitDistance;
	}
	
	int findCity(int mOpt, int mDestinations[])
	{
		//mOpt = 출근할 중심 도시의 갯수
		//mDestinations[] = 출근할 중심 도시의 ID 리스트
		for(int i = 0; i < mOpt; i++) {
			int trgtIdx = mDestinations[i]-1;
			candidates = new HashMap<>();
			int shortestId = bfs(trgtIdx, limitDistance);
		}
		
		return -1;
	}
	
	int bfs(int trgtIdx, int limitDistance) {
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean[] visited = new boolean[N];
		
		//Custom Min Heap
		PriorityQueue<Station> pq = new PriorityQueue<>(new Comparator<Station>() {

			@Override
			public int compare(Station o1, Station o2) {
				if(o1.price > o2.price) return 1;
				else {
					if(candidates.get(o1.id) > candidates.get(o2.id)) return 1;
					else {
						if(o1.id > o2.id) return 1;
						else return -1;
					}
				}
			}
			
		});
		
		
		visited[trgtIdx] = true;
		queue.offer(trgtIdx);
		
		while(!queue.isEmpty()) {
			int curr = queue.poll();
			Station s = stations.get(curr);
			int currDist = 0;
			
			if(trgtIdx == curr) currDist = 0;
			else currDist = candidates.get(s.id);
			
			for(int j = 0; j < s.next.size(); j++) {
				int distFromS = s.distances.get(j);
				
				Station n = s.next.get(j);
				int nextIdx = n.id-1;
				
				int distance = currDist + distFromS;
								
				if(!visited[nextIdx] && !candidates.containsKey(n.id) && !n.downtown && distance <= limitDistance) {
					visited[nextIdx] = true;
					candidates.put(n.id, distance);
					pq.add(n);
					queue.offer(nextIdx);
				}
			}
			System.out.println(">> candidates = " + candidates);
			
		}
		System.out.println(">> pq = " + pq);
		//printStations();
		
		return pq.poll().id;
	}
	
	void printStations() {
		for(Station s : stations) {
			System.out.println(s);
		}
	}
}