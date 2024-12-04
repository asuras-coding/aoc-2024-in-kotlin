fun main() {

    fun List<String>.to2DCharArray(): Array<CharArray> {
        val rows = this.size
        val columns = this.maxOfOrNull { it.length } ?: 0

        val charArray = Array(rows) { CharArray(columns) }

        for (i in 0..<rows) {
            for (j in 0..<columns) {
                charArray[i][j] = this[i][j]
            }
        }

        return charArray
    }

    fun Array<CharArray>.getSlice(vararg points: Pair<Int, Int>): String? {
        runCatching { return points.joinToString(separator = "") { p -> "${this[p.first][p.second]}" } }
        return null
    }

    fun findMas(i: Int, j: Int, array: Array<CharArray>): Int {
        return listOf(
            array.getSlice(i to j, i to j + 1, i to j + 2, i to j + 3),
            array.getSlice(i to j, i to j - 1, i to j - 2, i to j - 3),
            array.getSlice(i to j, i + 1 to j, i + 2 to j, i + 3 to j),
            array.getSlice(i to j, i - 1 to j, i - 2 to j, i - 3 to j),
            array.getSlice(i to j, i + 1 to j + 1, i + 2 to j + 2, i + 3 to j + 3),
            array.getSlice(i to j, i - 1 to j - 1, i - 2 to j - 2, i - 3 to j - 3),
            array.getSlice(i to j, i - 1 to j + 1, i - 2 to j + 2, i - 3 to j + 3),
            array.getSlice(i to j, i + 1 to j - 1, i + 2 to j - 2, i + 3 to j - 3)
        ).count { it == "XMAS" }
    }

    fun part1(input: List<String>): Int {
        val array = input.to2DCharArray()
        var counter = 0
        for (i in 0..array.lastIndex) {
            for (j in 0..array[i].lastIndex) {
                if (array[i][j] == 'X') {
                    counter += findMas(i, j, array)
                }
            }
        }
        return counter
    }

    fun find2Mas(i: Int, j: Int, array: Array<CharArray>): Int {
        val a = array.getSlice(i - 1 to j - 1, i to j, i + 1 to j + 1)
        val b = array.getSlice(i - 1 to j + 1, i to j, i + 1 to j - 1)
        if ((a == "MAS" || a == "SAM") && (b == "MAS" || b == "SAM")) return 1
        return 0
    }

    fun part2(input: List<String>): Int {
        val array = input.to2DCharArray()
        var counter = 0
        for (i in 0..array.lastIndex) {
            for (j in 0..array[i].lastIndex) {
                if (array[i][j] == 'A') {
                    counter += find2Mas(i, j, array)
                }
            }
        }
        return counter
    }

    val testInput = readInput("Day04_test")

    check(part1(testInput) == 18)
    check(part2(testInput) == 9)


    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}