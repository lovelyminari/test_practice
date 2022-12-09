import java.util.ArrayList;
import java.util.Arrays;
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
	int[] mDownTown;
	ArrayList<Station> stations;
	int limitDistance;
	Map<Integer, int[]> downtownDist;
	
	void init(int N, int mDownTown[]) {
		this.N = N;
		this.mDownTown = mDownTown;
		stations = new ArrayList<>();
		downtownDist = new HashMap<>();
		
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
		
		//printStations();
	}
	
	void changeLimitDistance(int mLimitDistance)
	{
		this.limitDistance = mLimitDistance;
		
		for(int i = 0; i < mDownTown.length; i++) {
			int trgtIdx = mDownTown[i]-1;
			int[] dist = dijkstra(trgtIdx);
			downtownDist.put(mDownTown[i], dist);
		}
		
		//System.out.println(">> downtownDist = " + downtownDist);
	}
	
	int findCity(int mOpt, int mDestinations[])
	{
		int[] dist = new int[N];	//출근 도시 기반의 최종 distance
		
		PriorityQueue<Station> minHeap = new PriorityQueue<>(new Comparator<Station>() {
			@Override
			public int compare(Station o1, Station o2) {
				if(o1.price < o2.price) return -1;
				else if(dist[o1.id-1] < dist[o2.id-1]) return -1;
				else if(o1.id < o2.id) return -1;
				else return 1;
			}
		});
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		//mOpt = 출근 도시 갯수
		//mDestinations[] = 출근 도시 ID
		for(int i = 0; i < mOpt; i++) {
			int[] dtDist = downtownDist.get(mDestinations[i]);
			//System.out.println(">> dtDist = " + Arrays.toString(dtDist));
			for(int j = 0; j < dist.length; j++) {
				dist[j] = (dist[j] == Integer.MAX_VALUE ? 0: dist[j]) + (dtDist[j] == Integer.MAX_VALUE ? 0 : dtDist[j]);
			}
		}
		
		System.out.println(">> final dist = " + Arrays.toString(dist));
		
		for(int i = 0; i < N; i++) {
			Station s = stations.get(i);
			minHeap.add(s);
		}
		
		System.out.println(">> minHeap = " + minHeap);
		
		for(int i = 0; i < N; i++) {
			Station candidate = minHeap.poll();
			
			if(!candidate.downtown && dist[candidate.id-1] <= limitDistance) {
				candidate.price++;
				System.out.println(">> candidate = " + candidate);
				printStations();
				return candidate.id;
			}
		}
		
		return -1;
	}
	
	int[] dijkstra(int trgtIdx) {
		int[] dist = new int[N];
		boolean[] visited = new boolean[N];
		
		//Custom Min Heap
		PriorityQueue<Station> pq = new PriorityQueue<>(new Comparator<Station>() {

			@Override
			public int compare(Station o1, Station o2) {
				if(dist[o1.id-1] < dist[o2.id-1]) return -1;
				else if(o1.id < o2.id) return -1;
				else return 1;
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
		System.out.println(">> " + (trgtIdx + 1) + " dist = " + Arrays.toString(dist));
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
