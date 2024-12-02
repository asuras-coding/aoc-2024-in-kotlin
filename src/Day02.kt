fun main() {
    fun part1(input: List<String>): Int {
        val reports = input.map{ line -> line.split(" ").map { it.toInt() } }
        return reports.map {it.windowed(2) { (a, b) -> a - b } }.filter { d -> d.all { it in 1..3 } || d.all { it in (-3)..(-1) }}.size
    }

    fun part2(input: List<String>): Int {
        val reports = input.map{ line -> line.split(" ").map { it.toInt() } }
        return reports.filter { report -> if (report.windowed(2) { (a, b) -> a - b }.all { it in 1..3 } || report.all { it in (-3)..(-1) }) return@filter true
            for (i in 0..report.lastIndex) {
                val tolerateWindows = (report.take(i) + report.drop(i+1)).windowed(2) { (a, b) -> a - b }
                if (tolerateWindows.all { it in 1..3 } || tolerateWindows.all { it in (-3)..(-1) }) return@filter true
            }
            return@filter false }.size
    }

    val testInput = readInput("Day02_test")

    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
