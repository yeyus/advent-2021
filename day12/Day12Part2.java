import java.util.*;
import javafx.util.*;

public class Day12Part2 {
	private Scanner sc;
	Map<String, Set<String>> edges;
	Set<List<String>> paths;

	public Day12Part2() {
		sc = new Scanner(System.in);
		sc.useDelimiter("\n");

		edges = new HashMap<>();
		paths = new HashSet<>();
	}

	// Add an undirected edge to the graph adjacency map
	private void addUndirectedEdge(String vertex1, String vertex2) {
		edges.putIfAbsent(vertex1, new HashSet<>());
		edges.putIfAbsent(vertex2, new HashSet<>());

		edges.get(vertex1).add(vertex2);
		edges.get(vertex2).add(vertex1);
	}

	public void parseInput() {
		while(sc.hasNext()) {
			String line = sc.next();
			String[] edge = line.split("-");
			addUndirectedEdge(edge[0], edge[1]);
		}
	}

	public int solve() {
		parseInput();

		System.out.println(edges);
		int result = dfs("start");

		return result;
	}

	class ProblemState {
		String vertex;
		List<String> path;
		HashSet<String> visited;
		String smallDoubleVisit = null;

		public ProblemState(String vertex, List<String> path, HashSet<String> visited, String smallDoubleVisit) {
			this.vertex = vertex;
			this.path = new LinkedList<>(path);
			this.visited = new HashSet<>(visited);
			this.smallDoubleVisit = smallDoubleVisit;
		}

		public boolean visitAndProceed(String vertex) {
			boolean isBigCave = Character.isUpperCase(vertex.charAt(0));
			
			if (isBigCave) {
				return true;
			} else if (!vertex.equals("start") && !vertex.equals("end") && smallDoubleVisit == null && visited.contains(vertex)) {
				this.smallDoubleVisit = vertex;
				return true;
			}

			boolean result = !visited.contains(vertex);
			visited.add(vertex);
			return result;
		}

		@Override
		public String toString() {
			return "ProblemState[vertex=" + vertex + " ,path=" + path + " ,visited=" + visited + " ,smallDoubleVisit=" + smallDoubleVisit +" ]";
		}
	}

	public int dfs(String start) {
		LinkedList<ProblemState> queue = new LinkedList<>();
		queue.offer(new ProblemState(start, new LinkedList(), new HashSet(), null));

		while(!queue.isEmpty()) {
			ProblemState current = queue.poll();
			
			// System.out.println(current);
			String vertex = current.vertex;
			List<String> path = current.path;

			path.add(vertex);

			if (vertex.equals("end")) {
				// System.out.println("*** THE END: " + path);
				paths.add(new LinkedList(path));
				continue;
			}

			// if visited is not a valid path
			if (!current.visitAndProceed(vertex)) continue;

			// process neighbors
			for(String n : edges.get(vertex)) {
				ProblemState nstate = new ProblemState(n, path, current.visited, current.smallDoubleVisit);
				// System.out.println("Queue offer: " + nstate);
				queue.offer(nstate);
			}
		}

		// System.out.println("Paths: " + paths);
		return paths.size();
	}

	public static void main(String[] args) {
		Day12Part2 p = new Day12Part2();
		System.out.println("The result is: " + p.solve());
	}

}
