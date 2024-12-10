fun main() {

    fun calcChecksum(disk: CharArray): Long =
        disk.foldIndexed(0L) { index, acc, c ->
            if (c == '.') acc else acc + index * (c - '0')
        }

    fun part1(input: List<String>): Long {
        var fileId = 0
        val disk = input.first()
            .mapIndexed { index, c -> if (index % 2 == 0) "${'0' + fileId++}".repeat(c.digitToInt()) else ".".repeat(c.digitToInt()) }.joinToString("").toCharArray()
        var forwardIndex = 0
        var backwardIndex = disk.lastIndex
        while (forwardIndex < backwardIndex) {
            if (disk[forwardIndex] == '.') {
                while (disk[backwardIndex] == '.') {
                    backwardIndex--
                }
                disk[forwardIndex] = disk[backwardIndex]
                disk[backwardIndex] = '.'
                backwardIndex--
            }
            forwardIndex++
        }
        return calcChecksum(disk)
    }


    fun part2(input: List<String>): Long {
        var fileId = 0
        val disk = input.first()
            .mapIndexed { index, c -> if (index % 2 == 0) FileItem(c.digitToInt(), fileId++) else EmptySpace(c.digitToInt()) }
        println(disk)
        return 0
//        return calcChecksum(disk)
    }

    val testInput = readInput("Day09_test")

    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

sealed interface DiskItem {
    val size: Int
}

data class FileItem(override val size: Int, val id: Int): DiskItem
data class EmptySpace(override val size: Int): DiskItem