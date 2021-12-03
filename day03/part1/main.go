package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	in := bufio.NewScanner(os.Stdin)

	ones := make([]int, 0, 32)
	total := 0

	for in.Scan() {
		text := in.Text()

		for len(ones) < len(text) {
			ones = append(ones, 0)
		}

		for i, c := range text {
			if c == '1' {
				ones[i]++
			}
		}
		total++
	}

	/* gamma rate is one if majority of bits for the position is 1 */
	gamma := 0

	/* epsilon rate is one if majority of bits for the position is 0, thus inverse non extended of gamma */
	epsilon := 0

	for _, v := range ones {
		gamma = gamma << 1
		epsilon = epsilon << 1
		if v > total/2 {
			gamma |= 1
		} else {
			epsilon |= 1
		}
	}

	fmt.Printf("%+v\n", ones)
	fmt.Printf("total: %d\n", total)
	fmt.Printf("gamma: %b\n", gamma)
	fmt.Printf("epsilon: %b\n", epsilon)
	fmt.Printf("result: %d\n", gamma*epsilon)
}
