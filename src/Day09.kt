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

    fun moveDiskPart(diskPart: DiskPart, disk: List<DiskPart>): List<DiskPart> {
        if (diskPart.fileSize == 0) return disk
        val candidate = disk.find { it.emptySpace >= diskPart.fileSize } ?: return disk

        val diskPartIndex = disk.indexOf(diskPart)
        val candIndex = disk.indexOf(candidate)

        val preIndex = diskPartIndex - 1
        val preItem = disk[preIndex]


        val newCandidate = candidate.copy(emptySpace = 0)
        val newDiskPart = diskPart.copy(emptySpace = candidate.emptySpace - diskPart.fileSize)
        val newPreItem = preItem.copy(emptySpace = preItem.emptySpace + diskPart.fileSize + diskPart.emptySpace)


        val newDisk = disk.toMutableList()
        newDisk.removeAt(diskPartIndex)
        newDisk.removeAt(preIndex)
        newDisk.add(preIndex, newPreItem)
        newDisk.removeAt(candIndex)
        newDisk.add(candIndex, newCandidate)
        newDisk.add(candIndex+1, newDiskPart)

//        val newDisk =
//            disk.subList(0, candIndex) +
//                    newCandidate + newDiskPart +
//                    disk.subList(candIndex + 1, preIndex) +
//                    newPreItem +
//                    disk.subList(diskPartIndex + 1, disk.size)
        return newDisk
    }

    fun isMoveable(diskPart: DiskPart, disk: List<DiskPart>): Boolean {
        return diskPart.fileSize > 0 && disk.subList(0, disk.indexOf(diskPart))
            .any { it.emptySpace >= diskPart.fileSize }
    }

    fun fillDiskSpaces(disk: List<DiskPart>): List<DiskPart> {
        var currentDisk = disk
        for (fileId in disk.map { it.fileId }.reversed()) {
            val diskPart = currentDisk.first { it.fileId == fileId }
            if (isMoveable(diskPart, currentDisk)) {
                currentDisk = moveDiskPart(diskPart, currentDisk)
            }
        }

        return currentDisk
    }

    fun part2(input: List<String>): Long {
        var fileId = 0
        val disk: List<DiskPart> = input.first().windowed(2, 2, partialWindows = true) {
            if (it.length < 2) {
                DiskPart(fileId++, it[0].digitToInt(), 0)
            } else {
                DiskPart(fileId++, it[0].digitToInt(), it[1].digitToInt())
            }
        }
        val filledDisk = fillDiskSpaces(disk)

        return calcChecksum(filledDisk.joinToString("") { it.print() }.toCharArray())
    }

    val testInput = readInput("Day09_test")

    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

data class DiskPart(val fileId: Int, val fileSize: Int, val emptySpace: Int) {
    fun print() = "${ '0' + fileId}".repeat(fileSize) + ".".repeat(emptySpace)
}