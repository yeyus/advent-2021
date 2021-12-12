import java.util.*;

public class Day11Part1 {
	private static final int[][] MOVES = {
		{-1,-1}, {-1, 0}, {-1, 1},
		{ 0,-1},          { 0, 1},
		{ 1,-1}, { 1, 0}, { 1, 1}
	};
	private Scanner sc;
 	private int[][] octopus;

	public Day11Part1() {
		sc = new Scanner(System.in);
	}

	public int[][] parseInput() {
		int[][] matrix = null;
		sc.useDelimiter("\n");

		int lines = 0;
		while(sc.hasNext()) {
			String line = sc.next();
		
			// initialize array as a square
			if (matrix == null) {
				matrix = new int[line.length()][line.length()];
			}

			for(int i = 0; i < line.length(); i++) {
				matrix[lines][i] = line.charAt(i) - '0'; 
			}

			lines += 1;
		}

		return matrix;
	}

	private void printOctopus() {
		for(int i = 0; i < octopus.length; i++) {
			for(int j = 0; j < octopus[i].length; j++) {
				System.out.print(octopus[i][j]);
			}
			System.out.print("\n");
		}
	}

	private int step(int[][] octopus) {
		boolean[][] flashed = new boolean[octopus.length][octopus[0].length];

		int flashes = 0;
		for(int i = 0; i < octopus.length; i++) {
			for(int j = 0; j < octopus[i].length; j++) {
				flashes += dfs(octopus, i, j, flashed);
			}
		}

		printOctopus();
		return flashes;
	}

	private int dfs(int[][] octopus, int i, int j, boolean[][] flashed) {
		Stack<int[]> stack = new Stack();
		int flashes = 0;

		stack.push(new int[] {i,j});

		while(!stack.isEmpty()) {
			int[] current = stack.pop();
			int ci = current[0];
			int cj = current[1];

			if (flashed[ci][cj]) continue;

			octopus[ci][cj] = (octopus[ci][cj] + 1) % 10;

			if (octopus[ci][cj] == 0) {
				flashes += 1;
				flashed[ci][cj] = true;
				
				for(int[] m : MOVES) {
					int ni = ci + m[0];
					int nj = cj + m[1];

					if (ni < 0 || ni >= octopus.length) continue;
					if (nj < 0 || nj >= octopus[ni].length) continue;

					stack.push(new int[]{ni,nj});
				}
			}
		}

		return flashes;
	}

	public int solve(int steps) {
		octopus = parseInput();

		int flashes = 0;
		for(int s = 1; s <= steps; s++) {
			System.out.println("After step " + s);
			flashes += step(octopus);
			System.out.println("Flashed " + flashes);
		}

		return flashes;
	}

	public static void main(String[] args) {
		Day11Part1 p = new Day11Part1();
		int steps = Integer.parseInt(args[0]);
		System.out.println("The result is: " + p.solve(steps));
	}
}
