fun main() {

    fun getAntennas(input: List<String>): Map<Char, List<Pair<Int, Int>>> {
        val rows = input.size
        val columns = input.maxOfOrNull { it.length } ?: 0

        val map = mutableMapOf<Char, List<Pair<Int, Int>>>()

        for (i in 0..<rows) {
            for (j in 0..<columns) {
                val c = input[i][j]
                if (c == '.') continue
                map.getOrDefault(c, listOf())
                map[input[i][j]] = map.getOrDefault(c, listOf()) + listOf(i to j)
            }
        }

        return map
    }

    fun isInBounds(bounds: Pair<Int, Int>, antinode: Pair<Int, Int>) =
        antinode.first in 0..<bounds.first && antinode.second in 0..<bounds.second

    fun part1(input: List<String>): Int {
        val bounds = input.size to input.maxOf { it.length }
        val antennas = getAntennas(input)
        val antinodes = mutableSetOf<Pair<Int, Int>>()
        for ((_, locations) in antennas) {
            for (a in locations) {
                for (b in locations.minus(a)) {
                    val distanceAB = b.first - a.first to b.second - a.second
                    val pAntinode = a.first + distanceAB.first * 2 to a.second + distanceAB.second * 2
                    if (isInBounds(bounds, pAntinode)) {
                        antinodes.add(pAntinode)
                    }
                }
            }
        }
        return antinodes.also { println(it) }.size
    }

    fun part2(input: List<String>): Int {
        val bounds = input.size to input.maxOf { it.length }
        val antennas = getAntennas(input)
        val antinodes = mutableSetOf<Pair<Int, Int>>()
        for ((_, locations) in antennas) {
            for (a in locations) {
                for (b in locations.minus(a)) {
                    val distance = b.first - a.first to b.second - a.second
                    var n = 2
                    var pAntinode = a.first + distance.first * n to a.second + distance.second * n
                    while (isInBounds(bounds, pAntinode)) {
                        antinodes.add(pAntinode)
                        n++
                        pAntinode = a.first + distance.first * n to a.second + distance.second * n
                    }
                }
            }
        }
        return (antinodes + antennas.flatMap { it.value }).size
    }

    val testInput = readInput("Day08_test")

    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}