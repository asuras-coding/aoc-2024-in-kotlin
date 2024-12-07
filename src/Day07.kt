fun main() {

    fun part1(input: List<String>): Long {
        val equations = input.map { it.substringBefore(": ").toLong() to it.substringAfter(": ").split(" ").map { it.toLong() } }
        return equations.filter { equation ->
            val (testValue, operands) = equation
            backtrackCheck(operands.first(), operands.drop(1), testValue)
        }.sumOf { it.first }
    }

    fun part2(input: List<String>): Long {
        val equations = input.map { it.substringBefore(": ").toLong() to it.substringAfter(": ").split(" ").map { it.toLong() } }
        return equations.filter { equation ->
            val (testValue, operands) = equation
            backtrackCheck2(operands.first(), operands.drop(1), testValue)
        }.sumOf { it.first }
    }

    val testInput = readInput("Day07_test")

    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

fun tryMul(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    val res = acc * remaining.first()
    if (res <= checkValue) return backtrackCheck(res, remaining.drop(1), checkValue)
    return false
}

fun tryAdd(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    val res = acc + remaining.first()
    if (res <= checkValue) return backtrackCheck(res, remaining.drop(1), checkValue)
    return false
}

fun backtrackCheck(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    if (remaining.isEmpty() && acc == checkValue) return true
    if (remaining.isEmpty()) return false
    if (tryMul(acc, remaining, checkValue)) return true
    if (tryAdd(acc, remaining, checkValue)) return true
    return false
}

fun tryConc2(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    val res = "$acc${remaining.first()}".toLong()
    if (res <= checkValue) return backtrackCheck2(res, remaining.drop(1), checkValue)
    return false
}

fun tryMul2(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    val res = acc * remaining.first()
    if (res <= checkValue) return backtrackCheck2(res, remaining.drop(1), checkValue)
    return false
}

fun tryAdd2(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    val res = acc + remaining.first()
    if (res <= checkValue) return backtrackCheck2(res, remaining.drop(1), checkValue)
    return false
}

fun backtrackCheck2(acc: Long, remaining: List<Long>, checkValue: Long): Boolean {
    if (remaining.isEmpty() && acc == checkValue) return true
    if (remaining.isEmpty()) return false
    if (tryConc2(acc, remaining, checkValue)) return true
    if (tryMul2(acc, remaining, checkValue)) return true
    if (tryAdd2(acc, remaining, checkValue)) return true
    return false
}