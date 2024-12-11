fun main() {

    fun blink(stones: List<Long>): List<Long> {
        return buildList {
            for (stone in stones) {
                val stoneString = stone.toString()
                when {
                    stone == 0L -> add(1L)
                    stoneString.length % 2 == 0 -> {
                        listOf(
                            stoneString.take(stoneString.length / 2).toLong(),
                            stoneString.takeLast(stoneString.length / 2).toLong()
                        ).also { addAll(it) }
                    }

                    else -> listOf(stone * 2024L).also { addAll(it) }
                }
            }
        }
    }


    fun part1(input: List<String>): Int {
        var stones = input.first().split(" ").map { it.toLong() }
        repeat(25) {
            stones = blink(stones)
        }
        return stones.size
    }

    fun part2(input: List<String>): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        fun countBlinks(stone: Long, blinks: Int): Long {
            val stoneStr = stone.toString()
            return when {
                blinks == 0 -> cache.getOrPut(stone to 0) { 1L }
                stone == 0L -> cache.getOrPut(0L to blinks) { countBlinks(1L, blinks - 1) }
                stoneStr.length % 2 == 0 -> cache.getOrPut(stone to blinks) {
                    val left = stoneStr.take(stoneStr.length / 2)
                    val right = stoneStr.takeLast(stoneStr.length / 2)
                    countBlinks(left.toLong(), blinks - 1) + countBlinks(right.toLong(), blinks - 1)
                }

                else -> cache.getOrPut(stone to blinks) {
                    countBlinks(stone * 2024L, blinks - 1)
                }
            }
        }

        val stones = input.first().split(" ").map { it.toLong() }
        return stones.sumOf { stone -> countBlinks(stone, 75) }
    }

    val testInput = readInput("Day11_test")

    check(part1(testInput) == 55312)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}