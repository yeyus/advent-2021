import java.util.*;

class Day10Part2 {
	private Scanner sc;
	private Stack<Character> stack;
	private PriorityQueue<Long> heap;
	private static Map<Character, Character> closingPairs = new HashMap<>(); 
	private static Map<Character, Integer> points = new HashMap<>();

	public Day10Part2() {
		sc = new Scanner(System.in);
		stack = new Stack<>();
		heap = new PriorityQueue<>();

		closingPairs.put('(', ')');
		closingPairs.put('{', '}');
		closingPairs.put('<', '>');
		closingPairs.put('[', ']');

		points.put(')', 1);
		points.put(']', 2);
		points.put('}', 3);
		points.put('>', 4);
	}

	public long solve() {
		sc.useDelimiter("\n");

		while(sc.hasNext()) {
			long result = 0;
			boolean fail = false;
			stack.clear();

			// Process a single line
			String line = sc.next();
			char[] chars = line.toCharArray();

			for(char c : chars) {
				if (c == '(' || c == '[' || c == '{' || c == '<') {
					stack.push(closingPairs.get(c));
				} else {
					char expected = stack.pop();
					if (expected != c) {
						fail = true;
						break;
					}
				}
			}

			// we need this so we ignore lines with an invalid combination
			if (fail || stack.isEmpty()) continue;

			while(!stack.isEmpty()) {
				result *= 5;
				result += points.get(stack.pop());
			}

			heap.offer(result);
		}

		System.out.println("The size of the heap is: " + heap.size());
		int median = (int)Math.ceil(heap.size() / 2);
		for (int i = 0; i < median; i++) {
			long v = heap.poll();
			System.out.println(i + " is " + v);
		}

		return heap.poll();
	}

	public static void main(String[] args) {
		Day10Part2 problem = new Day10Part2();
		System.out.println("The result is " + problem.solve());
	}
}
