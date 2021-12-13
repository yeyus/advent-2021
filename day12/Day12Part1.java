import java.util.*;
import javafx.util.*;

public class Day12Part1 {
	private Scanner sc;
	Map<String, Set<String>> edges;
	Set<List<String>> paths;

	public Day12Part1() {
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
		Set<String> visited;

		public ProblemState(String vertex, List<String> path, Set<String> visited) {
			this.vertex = vertex;
			this.path = new LinkedList<>(path);
			this.visited = new HashSet<>(visited);
		}

		@Override
		public String toString() {
			return "ProblemState[vertex=" + vertex + " ,path=" + path + " ,visited=" + visited + "]";
		}
	}

	public int dfs(String start) {
		LinkedList<ProblemState> queue = new LinkedList<>();
		queue.offer(new ProblemState(start, new LinkedList(), new HashSet()));

		while(!queue.isEmpty()) {
			ProblemState current = queue.poll();
			
			// System.out.println(current);
			String vertex = current.vertex;
			List<String> path = current.path;
			Set<String> visited = current.visited;

			path.add(vertex);

			if (vertex.equals("end")) {
				// System.out.println("*** THE END: " + path);
				paths.add(new LinkedList(path));
				continue;
			}

			// if visited is not a valid path
			if (visited.contains(vertex)) continue;

			// Lowercase (small caves) can only be visited once
			if (Character.isLowerCase(vertex.charAt(0))) {
				visited.add(vertex);
			}

			// process neighbors
			for(String n : edges.get(vertex)) {
				ProblemState nstate = new ProblemState(n, path, visited);
				//System.out.println("Queue offer: " + nstate);
				queue.offer(nstate);
			}
		}

		System.out.println("Paths: " + paths);
		return paths.size();
	}

	public static void main(String[] args) {
		Day12Part1 p = new Day12Part1();
		System.out.println("The result is: " + p.solve());
	}

}
