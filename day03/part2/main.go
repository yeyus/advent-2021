package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"sort"
	"strconv"
)

type TreeNode struct {
	zeroCount int
	zeroNode  *TreeNode
	oneCount  int
	oneNode   *TreeNode
}

func (t *TreeNode) String() string {
	return fmt.Sprintf("Node{ zeros:%d [%s], ones: %d [%s] }", t.zeroCount, t.zeroNode, t.oneCount, t.oneNode)
}

func NewTreeNode() *TreeNode {
	return &TreeNode{}
}

func main() {
	in := bufio.NewScanner(os.Stdin)

	numbers := make([]int, 0, 1000)

	bitLength := 0

	for in.Scan() {
		text := in.Text()
		bitLength = len(text)
		n, err := strconv.ParseInt(text, 2, 32)
		if err != nil {
			log.Panicf("Something is not a number: %s\n", text)
		}

		numbers = append(numbers, int(n))
	}

	sort.Ints(numbers)

	// print all sorted values
	// for _, v := range numbers {
	// 	fmt.Printf("%0*b\n", bitLength, v)
	// }

	// create decision tree
	node := createNode(numbers, bitLength-1, 0, len(numbers))
	//fmt.Printf("%s\n", node)

	// find oxygen generator value
	// we follow the most common value
	oxygen := make([]int, 0, 32)
	traverseMax(node, &oxygen)
	fmt.Printf("Oxygen: %+v\n", oxygen)
	sliceRightExtend(&oxygen, bitLength)
	fmt.Printf("Oxygen extended: %+v\n", oxygen)
	oxygenInt := sliceFindNext(numbers, sliceToInt(oxygen))
	fmt.Printf("Oxygen int: %d\n", oxygenInt)

	// find co2 value
	// we follow the least common value
	co2 := make([]int, 0, 32)
	traverseMin(node, &co2)
	fmt.Printf("CO2 Scrubber: %+v\n", co2)
	sliceRightExtend(&co2, bitLength)
	fmt.Printf("CO2 Scrubber extended: %+v\n", co2)
	co2Int := sliceFindNext(numbers, sliceToInt(co2))
	fmt.Printf("CO2 int: %d\n", co2Int)

	fmt.Printf("Result: %d\n", oxygenInt*co2Int)
}

func createNode(numbers []int, bitPosition int, start int, end int) *TreeNode {
	if bitPosition < 0 {
		return nil
	}
	if start >= end {
		return nil
	}

	i := 0
	for i = start; i < end; i++ {
		bit := (numbers[i] >> bitPosition) & 1
		if bit > 0 {
			break
		}
	}

	node := NewTreeNode()
	node.zeroCount = i - start
	node.zeroNode = createNode(numbers, bitPosition-1, start, i)
	node.oneCount = end - i
	node.oneNode = createNode(numbers, bitPosition-1, i, end)

	return node
}

func traverseMax(root *TreeNode, decisions *[]int) {
	if maxInt(root.zeroCount, root.oneCount) == 1 && minInt(root.zeroCount, root.oneCount) == 0 {
		return
	}

	if root.oneCount >= root.zeroCount {
		*decisions = append(*decisions, 1)
		if root.oneNode != nil {
			traverseMax(root.oneNode, decisions)
		}
	} else {
		*decisions = append(*decisions, 0)
		if root.zeroNode != nil {
			traverseMax(root.zeroNode, decisions)
		}
	}
}

func traverseMin(root *TreeNode, decisions *[]int) {
	if maxInt(root.zeroCount, root.oneCount) == 1 && minInt(root.zeroCount, root.oneCount) == 0 {
		return
	}

	if root.oneCount < root.zeroCount {
		*decisions = append(*decisions, 1)
		if root.oneNode != nil {
			traverseMin(root.oneNode, decisions)
		}
	} else {
		*decisions = append(*decisions, 0)
		if root.zeroNode != nil {
			traverseMin(root.zeroNode, decisions)
		}
	}
}

func sliceToInt(slice []int) int {
	val := 0
	for _, b := range slice {
		val = val << 1
		if b > 0 {
			val |= 1
		}
	}

	return val
}

func sliceRightExtend(slice *[]int, size int) {
	for len(*slice) < size {
		*slice = append(*slice, 0)
	}
}

func sliceFindNext(slice []int, target int) int {
	var v int
	for _, v = range slice {
		if v >= target {
			break
		}
	}

	return v
}

func maxInt(a int, b int) int {
	if a > b {
		return a
	}
	return b
}

func minInt(a int, b int) int {
	if a > b {
		return b
	}
	return a
}
