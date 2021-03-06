import sys

def main():
    depth = 0
    length = 0

    for line in sys.stdin:
        tokens = line.split(' ')
        command = tokens[0]
        value = int(tokens[1])
        
        if command == "forward":
            length += value
        elif command == "up":
            depth -= value
        elif command == "down":
            depth += value
        else:
            print("Unknown command")
    
    print(depth * length)

if __name__ == "__main__":
    main()