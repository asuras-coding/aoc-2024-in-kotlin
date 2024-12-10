fun main() {
    fun List<String>.to2DIntArray(): Array<IntArray> {
        val rows = this.size
        val columns = this.maxOfOrNull { it.length } ?: 0

        val intArray = Array(rows) { IntArray(columns) }

        for (y in 0..<rows) {
            for (x in 0..<columns) {
                intArray[y][x] = this[y][x].digitToInt()
            }
        }

        return intArray
    }

    fun getAdjacentNodes(value: Int, x: Int, y: Int, grid: Array<IntArray>): List<Node> {
        return listOfNotNull(
            grid.getOrNull(y)?.getOrNull(x + 1)?.let { if (it == value + 1) Node(it, x + 1, y, getAdjacentNodes(it, x + 1, y, grid)) else null },
            grid.getOrNull(y)?.getOrNull(x - 1)?.let { if (it == value + 1) Node(it, x - 1, y, getAdjacentNodes(it, x - 1, y, grid)) else null },
            grid.getOrNull(y + 1)?.getOrNull(x)?.let { if (it == value + 1) Node(it, x, y + 1, getAdjacentNodes(it, x, y + 1, grid)) else null },
            grid.getOrNull(y - 1)?.getOrNull(x)?.let { if (it == value + 1) Node(it, x, y - 1, getAdjacentNodes(it, x, y - 1, grid)) else null },
        )
    }

    fun getHighpoints(node: Node): List<Node> {
        if (node.value == 9) return listOf(node)
        return node.adjacentNodes.flatMap { getHighpoints(it)}
    }

    fun part1(input: List<String>): Int {
        val grid = input.to2DIntArray()
        val tailheads = mutableListOf<Node>()
        for (y in 0..grid.lastIndex) {
            for (x in 0..grid[y].lastIndex) {
                if (grid[y][x] == 0) tailheads.add(Node(0, x, y, getAdjacentNodes(0, x, y, grid)))
            }
        }
        return tailheads.sumOf { getHighpoints(it).distinct().size }
    }

    fun part2(input: List<String>): Int {
        val grid = input.to2DIntArray()
        val tailheads = mutableListOf<Node>()
        for (y in 0..grid.lastIndex) {
            for (x in 0..grid[y].lastIndex) {
                if (grid[y][x] == 0) tailheads.add(Node(0, x, y, getAdjacentNodes(0, x, y, grid)))
            }
        }
        return tailheads.sumOf { getHighpoints(it).size }
    }

    val testInput = readInput("Day10_test")

    // Or read a large test input from the `src/Day01_test.txt` file:
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    // Read the input from the `src/Day01.txt` file.

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class Node(val value: Int, val x: Int, val y: Int, val adjacentNodes: List<Node>)