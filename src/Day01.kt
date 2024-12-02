import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (left, right) = input.map{ line -> line.split(Regex("\\s+")).let { it.first().toInt() to it.last().toInt() }}.unzip()
        return left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l - r) }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = input.map{ line -> line.split(Regex("\\s+")).let { it.first().toInt() to it.last().toInt() }}.unzip()
        val frequencies = right.groupingBy { it }.eachCount()
        return left.fold(0) { acc, value -> acc + frequencies.getOrDefault(value, 0) * value }
    }

    val testInput = readInput("Day01_test")

    // Or read a large test input from the `src/Day01_test.txt` file:
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}