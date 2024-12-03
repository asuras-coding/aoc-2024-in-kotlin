fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            """mul\(\d{1,3},\d{1,3}\)""".toRegex().findAll(line).map { it.value }.toList().sumOf { it.drop(4).dropLast(1).split(",").map { numStr -> numStr.toLong() }.reduce(Long::times) }
        }
    }

    fun part2(input: List<String>): Long {
        var mul = true
        var result = 0L
        for (line in input) {
            val statements = """do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)""".toRegex().findAll(line)
            for (statement in statements) {
                if (statement.value == "do()") {
                    mul = true
                } else if (statement.value == "don't()") {
                    mul = false
                } else if (mul) {
                    result += statement.value.substringAfter("(").substringBefore(")").split(",").map { numStr -> numStr.toLong() }.reduce(Long::times)
                }
            }
        }
        return result
    }

    fun part2CgptFunctional(input: List<String>): Long =
        input.flatMap { line ->
            """do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)""".toRegex().findAll(line).map { it.value }
        }.fold(Pair(true, 0L)) { (mul, result), statement ->
            when {
                statement == "do()" -> Pair(true, result)
                statement == "don't()" -> Pair(false, result)
                mul && statement.startsWith("mul") -> {
                    val product = statement.substringAfter("(").substringBefore(")").split(",").map(String::toLong).reduce(Long::times)
                    Pair(mul, result + product)
                }
                else -> Pair(mul, result)
            }
        }.second

    val testInput = readInput("Day03_test")

    check(part1(testInput) == 161L)
    check(part2(testInput) == 48L)


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}