import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

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
	int[] mDownTown;
	ArrayList<Station> stations;
	int limitDistance;
	Map<Integer, int[]> downtownDist;
	HashMap<Integer, PriorityQueue<Station>> minHeaps;
	
	void init(int N, int mDownTown[]) {
		this.N = N;
		this.mDownTown = mDownTown;
		stations = new ArrayList<>();
		downtownDist = new HashMap<>();
		minHeaps = new HashMap<>();
		
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
		
		//각 주요 도시의 distance 구하기
		for(int i = 0; i < mDownTown.length; i++) {
			int trgtIdx = mDownTown[i]-1;
			int[] dist = dijkstra(trgtIdx);
			downtownDist.put(mDownTown[i], dist);
		}
		
		/** 각 케이스에 대한 distance 합 구하기 */
		//주요도시 1개만 출근하는 경우 3개(위에서 이미 구함), 2개 출근하는 경우 3개, 3개 출근하는 경우 1개
		//2개 출근하는 경우(3가지)
		for(int i = 0; i < mDownTown.length; i++) {
			int[] iDist = downtownDist.get(mDownTown[i]);
			
			for(int j = 0; j < mDownTown.length; j++) {
				if(i == j) continue;
				
				if(downtownDist.containsKey(mDownTown[i] + mDownTown[j])) continue;
				
				int[] dist = new int[N];
				Arrays.fill(dist, Integer.MAX_VALUE);
				
				int[] jDist = downtownDist.get(mDownTown[j]);
				
				for(int z = 0; z < dist.length; z++) {
					dist[z] = (iDist[z] == Integer.MAX_VALUE ? 0: iDist[z]) + (jDist[z] == Integer.MAX_VALUE ? 0 : jDist[z]);
				}
				
				downtownDist.put(mDownTown[i] + mDownTown[j], dist);
			}
		}
		
		//3개 출근하는 경우
		int[] dist_0 = downtownDist.get(mDownTown[0]);
		int[] dist_1 = downtownDist.get(mDownTown[1]);
		int[] dist_2 = downtownDist.get(mDownTown[2]);
		
		int[] dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		for(int i = 0; i < dist.length; i++) {
			dist[i] = (dist_0[i] == Integer.MAX_VALUE ? 0: dist_0[i]) + (dist_1[i] == Integer.MAX_VALUE ? 0 : dist_1[i]) + (dist_2[i] == Integer.MAX_VALUE ? 0 : dist_2[i]);
		}
		
		downtownDist.put(mDownTown[0] + mDownTown[1] + mDownTown[2], dist);
		
		//printStations();
	}
	
	void changeLimitDistance(int mLimitDistance)
	{
		//System.out.println(">> changeLimitDistance");
		this.limitDistance = mLimitDistance;
		
		int case_1 = 1;
		int case_2 = 2;
		int case_3 = 3;
		int case_4 = case_1 + case_2;	//3
		int case_5 = case_1 + case_3;	//4
		int case_6 = case_2 + case_3;	//5
		int case_7 = case_1 + case_2 + case_3;	//6
		
		Set<Integer> keys = downtownDist.keySet();
		//System.out.println(">> keys = " + keys);
		
		for(Integer key : keys) {
			int[] dist = downtownDist.get(key);
			//System.out.println(key + " >> dist = " + Arrays.toString(dist));
			
			PriorityQueue<Station> minHeap = new PriorityQueue<>(new Comparator<Station>() {
				@Override
				public int compare(Station o1, Station o2) {
					
					System.out.println(">>>> compare " + o1.id + " " + o2.id);
					
					System.out.println(o1); System.out.println(o2);
					System.out.println("o1.price - o2.price = " + (o1.price - o2.price));
					System.out.println("dist[o1.id-1], dist[o2.id-1] = " + dist[o1.id-1] + ", " + dist[o2.id-1]);
					  // System.out.println("o1.id - o2.id = " + (o1.id - o2.id));
					
					if(o1.price == o2.price) {
						System.out.println("dist[o1.id-1] - dist[o2.id-1] = " + (dist[o1.id-1] - dist[o2.id-1]));
						if(dist[o1.id-1] == dist[o2.id-1]) {return o1.id - o2.id;}
						else {return dist[o1.id-1] - dist[o2.id-1];}
					}
					
					return o1.price - o2.price;
				}
			});
			
			for(int i = 0; i < N; i++) {
				Station s = stations.get(i);
				
				if(!s.downtown && dist[s.id-1] <= limitDistance) {

					//System.out.println(">> s.id = " + s.id);
					//System.out.println(s);
					minHeap.offer(s);
				}
			}
			
			//System.out.println(">> minHeap = " + minHeap);
			minHeaps.put(key, minHeap);
		}
		
		//System.out.println(">> downtownDist = " + downtownDist);
	}
	
	int findCity(int mOpt, int mDestinations[])
	{
		//System.out.println(">> final dist = " + Arrays.toString(dist));
		
		int key = 0;
		for(int i = 0; i < mOpt; i++) {
			key += mDestinations[i];
		}
		
		PriorityQueue<Station> minHeap = minHeaps.get(key);
		System.out.println(key + " >> minHeap1 = " + minHeap);
		
		if(minHeap.isEmpty()) return -1;
		
		Station candidate = minHeap.poll();
		
		System.out.println(">> candidate = " + candidate);
		//printStations();
		candidate.price++;
		
		for(int i = 0; i < minHeap.size(); i++) {
			Station s = minHeap.poll();
			minHeap.offer(s);
		}
		
		minHeap.offer(candidate);
		
		return candidate.id;
	}
	
	int[] dijkstra(int trgtIdx) {
		int[] dist = new int[N];
		boolean[] visited = new boolean[N];
		
		//Custom Min Heap
		PriorityQueue<Station> pq = new PriorityQueue<>(new Comparator<Station>() {

			@Override
			public int compare(Station o1, Station o2) {
				return dist[o1.id-1] - dist[o2.id-1];
			}
			
		});

		Arrays.fill(dist, Integer.MAX_VALUE);
		
		dist[trgtIdx] = 0;
		pq.offer(stations.get(trgtIdx));
		
		while(!pq.isEmpty()) {
			Station curr = pq.poll();
			int currDist = dist[curr.id-1];
			
			if(visited[curr.id-1]) continue;
			
			visited[curr.id-1] = true;
			
			for(int j = 0; j < curr.next.size(); j++) {
				int distFromCurr = curr.distances.get(j);
				
				Station n = curr.next.get(j);
				
				int distance = currDist + distFromCurr;
				
				if(!visited[n.id-1] && distance < dist[n.id-1]) {
					dist[n.id-1] = distance;
					pq.offer(n);
				}
			}
			
		}
		//System.out.println(">> " + (trgtIdx + 1) + " dist = " + Arrays.toString(dist));
		//printStationId();
		return dist;
	}
	
	void printStations() {
		for(Station s : stations) {
			System.out.println(s);
		}
	}
	
	void printStationId() {
		for(Station s : stations) {
			System.out.println(s.id);
		}
	}
}
