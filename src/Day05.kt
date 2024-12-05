fun main() {
    fun checkUpdate(update: Map<Int, Int>, rules: List<Pair<Int, Int>>): Boolean {
        for (rule in rules) {
            if ((update[rule.first] ?: continue) > (update[rule.second] ?: continue)) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val rules = input.takeWhile { it.isNotBlank() }.map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }
        val updates: List<Pair<List<Int>, Map<Int, Int>>> = input.takeLastWhile { it.isNotBlank() }.map {
            val list = it.split(",").map { it.toInt()}
            list to list.mapIndexed { index, i -> i to index }.toMap()
        }
        return updates.filter { update -> checkUpdate(update.second, rules) }.sumOf { it.first[it.first.lastIndex / 2] }
    }

    fun repairUpdate(update: List<Int>, groupedRules: Map<Int, List<Int>>): List<Int> {
        var inProgress = update.toMutableList()
        for (number in update) {
            val rule = groupedRules[number] ?: continue
            val numberIndex = inProgress.indexOf(number)
            val maxIndex = rule.maxOf { inProgress.indexOf(it) }
            if (maxIndex > 0 && maxIndex > numberIndex) {
                inProgress.removeAt(numberIndex)
                inProgress.add(maxIndex, number)
            }
        }
        return inProgress
    }

    fun part2(input: List<String>): Int {
        val rules: List<Pair<Int, Int>> = input.takeWhile { it.isNotBlank() }.map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }
        val updates: List<Pair<List<Int>, Map<Int, Int>>> = input.takeLastWhile { it.isNotBlank() }.map {
            val list = it.split(",").map { it.toInt()}
            list to list.mapIndexed { index, i -> i to index }.toMap()
        }
        val invalidUpdates = updates.filterNot { update -> checkUpdate(update.second, rules) }
        val groupedRulesDec = input.takeWhile { it.isNotBlank() }.map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }.groupBy({ it.second }, { it.first })

        return invalidUpdates.map { repairUpdate(it.first, groupedRulesDec) }.sumOf{ it[it.lastIndex / 2] }
    }

    val testInput = readInput("Day05_test")

    check(part1(testInput) == 143)
    check(part2(testInput) == 123)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}