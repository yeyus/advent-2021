import java.util.*;

class Day10Part1 {
	private Scanner sc;
	private Stack<Character> stack;
	private static Map<Character, Character> closingPairs = new HashMap<>(); 
	private static Map<Character, Integer> points = new HashMap<>();

	public Day10Part1() {
		sc = new Scanner(System.in);
		stack = new Stack<>();

		closingPairs.put('(', ')');
		closingPairs.put('{', '}');
		closingPairs.put('<', '>');
		closingPairs.put('[', ']');

		points.put(')', 3);
		points.put(']', 57);
		points.put('}', 1197);
		points.put('>', 25137);
	}

	public int solve() {
		int result = 0;
		sc.useDelimiter("\n");

		while(sc.hasNext()) {
			// Process a single line
			String line = sc.next();
			char[] chars = line.toCharArray();

			for(char c : chars) {
				if (c == '(' || c == '[' || c == '{' || c == '<') {
					stack.push(closingPairs.get(c));
				} else {
					char expected = stack.pop();
					if (expected != c) {
						result += points.get(c);
						break;
					}
				}
			}
		}

		return result;
	}

	public static void main(String[] args) {
		Day10Part1 problem = new Day10Part1();
		System.out.println("The result is " + problem.solve());
	}
}
