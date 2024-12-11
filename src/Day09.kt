fun main() {

    fun calcChecksum(disk: CharArray): Long =
        disk.foldIndexed(0L) { index, acc, c ->
            if (c == '.') acc else acc + index * (c - '0')
        }

    fun part1(input: List<String>): Long {
        var fileId = 0
        val disk = input.first()
            .mapIndexed { index, c -> if (index % 2 == 0) "${'0' + fileId++}".repeat(c.digitToInt()) else ".".repeat(c.digitToInt()) }
            .joinToString("").toCharArray()
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

    fun tryMove(partToMove: DiskPart, disk: MutableList<DiskPart>) {
        val partToMoveIndex = disk.indexOf(partToMove)
        val emptySpaceIndex = disk.subList(0, partToMoveIndex).indexOfFirst { it.emptySpace >= partToMove.fileSize }
        if (emptySpaceIndex == -1) return
        val itemBeforePartToMove = partToMoveIndex -1
        disk[itemBeforePartToMove].emptySpace += partToMove.fileSize + partToMove.emptySpace
        val itemWithEmptySpace = disk[emptySpaceIndex]
        partToMove.emptySpace = itemWithEmptySpace.emptySpace - partToMove.fileSize
        itemWithEmptySpace.emptySpace = 0
        disk.removeAt(partToMoveIndex)
        disk.add(emptySpaceIndex +1, partToMove)
    }

    fun fillDiskSpaces(maxFileId: Int, disk: List<DiskPart>): List<DiskPart> {
        val currentDisk = disk.toMutableList()
        for (fileId in maxFileId downTo 0) {
            val diskPart = currentDisk.find { it.fileId == fileId } ?: throw IllegalStateException("No disk part found for file id $fileId")
            tryMove(diskPart, currentDisk)
        }

        return currentDisk
    }

    fun part2(input: List<String>): Long {
        var fileId = -1
        val disk: List<DiskPart> = input.first().windowed(2, 2, partialWindows = true) {
            if (it.length < 2) {
                DiskPart(++fileId, it[0].digitToInt(), 0)
            } else {
                DiskPart(++fileId, it[0].digitToInt(), it[1].digitToInt())
            }
        }
        val filledDisk = fillDiskSpaces(fileId, disk)

        return calcChecksum(filledDisk.joinToString("") { it.print() }.toCharArray())
    }

    val testInput = readInput("Day09_test")

    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

data class DiskPart(val fileId: Int, val fileSize: Int, var emptySpace: Int) {
    fun print() = "${'0' + fileId}".repeat(fileSize) + ".".repeat(emptySpace)
}