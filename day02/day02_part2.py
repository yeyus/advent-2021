import sys

def main():
    depth = 0
    length = 0
    aim = 0

    for line in sys.stdin:
        tokens = line.split(' ')
        command = tokens[0]
        value = int(tokens[1])
        
        if command == "forward":
            length += value
            depth += aim * value
        elif command == "up":
            aim -= value
        elif command == "down":
            aim += value
        else:
            print("Unknown command")
    
    print(depth * length)

if __name__ == "__main__":
    main()