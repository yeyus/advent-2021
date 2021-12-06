//package com.ea7jmf.advent2021.day5;

import java.util.*;

public class Day5Part1 {

	class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Point(String str) {
			String[] tokens = str.trim().split(",");
			x = Integer.parseInt(tokens[0], 10);
			y = Integer.parseInt(tokens[1], 10);
		}
		
		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}

			if (!(other instanceof Point)) {
				return false;
			}

			Point otherPoint = (Point)other;
			return otherPoint.x == x && otherPoint.y == y;
		}

		@Override
		public int hashCode() {
			return (Integer.toString(x) + "," + Integer.toString(y)).hashCode();
		}

		@Override
		public String toString() {
			return "Point[x=" + x + ",y=" + y+ "]"; 
		}
	}

	class Line implements Iterable<Point> {
		Point start;
		Point end;

		public Line(String line) {
			String[] cords = line.split(" -> ");

			start = new Point(cords[0]);
			end = new Point(cords[1]);
		}

		public String toString() {
			return "Line[from=" + start + " -> to=" + end + "]";
		}

		
		public Iterator<Point> iterator() {
			Iterator<Point> it = new Iterator<Point>() {
				int count = 0;

				@Override
				public boolean hasNext() {
					return count <= length();
				}

				@Override
				public Point next() {
					int x = start.x;
					if (start.x < end.x) {
						x += count;
					} else if (start.x > end.x) {
						x -= count;
					}

					int y = start.y;
					if (start.y < end.y) {
						y += count;
					} else if (start.y > end.y) {
						y -= count;
					}

					count++;

					return new Point(x,y);
				}
			};

			return it;
		}

		public int length() {
			int x = Math.abs(end.x - start.x);
			int y = Math.abs(end.y - start.y);
			return Math.max(y, x);
		}

		public boolean isHorizontal() {
			return start.x == end.x;
		}

		public boolean isVertical() {
			return start.y == end.y;
		}

		public boolean belongs(Point p) {
			if (this.isHorizontal()) {
				for (int i = this.start.x; i < this.end.x; i++) {
					if (p.y == this.start.y && p.x == i) return true;
				}
			} else if (this.isVertical()) {
				for (int i = this.start.y; i < this.end.y; i++) {
					if (p.x == this.start.x && p.y == i) return true;
				}
			}

			return false;
		}
	}

	public static void main(String[] args) {
		Day5Part1 problem = new Day5Part1();
		problem.solve();
	}
 
	Scanner sc;
	
	HashSet<Point> points;
	HashSet<Point> result;

	public Day5Part1() {
		sc = new Scanner(System.in);
		points = new HashSet();
		result = new HashSet();
	}

	public void solve() {
		sc.useDelimiter("\n");

		while(sc.hasNext()) {
			String line = sc.next();
			Line l = new Line(line);
			
			// enable for part1
			//if (!(l.isHorizontal() || l.isVertical())) continue;
			
			//System.out.println("Line found " + l + " length= " + l.length());

			for (Point p : l) {
				//System.out.println("\t " + p);
				if (!points.add(p)) {
					result.add(p);
				}
			}
		}

		// list conflict points
		System.out.println(result);

		System.out.println("Result is: " + result.size());

		sc.close();
	}
}
