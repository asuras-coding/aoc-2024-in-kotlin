fun main() {

    fun List<String>.to2DCharArray(): Pair<Array<CharArray>, GuardPos> {
        var guardPos = GuardPos(0, 0, Direction.LEFT)
        val rows = this.size
        val columns = this.maxOfOrNull { it.length } ?: 0

        val charArray = Array(rows) { CharArray(columns) }

        for (i in 0..<rows) {
            for (j in 0..<columns) {
                when (this[i][j]) {
                    '<' -> guardPos = GuardPos(i, j, Direction.LEFT)
                    '>' -> guardPos = GuardPos(i, j, Direction.RIGHT)
                    '^' -> guardPos = GuardPos(i, j, Direction.UP)
                    'v' -> guardPos = GuardPos(i, j, Direction.DOWN)
                }
                charArray[i][j] = this[i][j]
            }
        }

        return charArray to guardPos
    }

    fun moveGuard(grid: Array<CharArray>, guardPos: GuardPos): GuardPos? {
        runCatching {
            val adjacentPos = when (guardPos.direction) {
                Direction.LEFT -> guardPos.x to guardPos.y - 1
                Direction.RIGHT -> guardPos.x to guardPos.y + 1
                Direction.UP -> guardPos.x - 1 to guardPos.y
                Direction.DOWN -> guardPos.x + 1 to guardPos.y
            }
            return when (grid[adjacentPos.first][adjacentPos.second]) {
                '#', 'O' -> guardPos.copy(direction = guardPos.direction.turn())
                else -> GuardPos(adjacentPos.first, adjacentPos.second, guardPos.direction)
            }
        }
        return null
    }


    fun calculateGuardPath(grid: Array<CharArray>, initialGuardPos: GuardPos): Pair<Set<GuardPos>, Boolean> {
        var guardPos: GuardPos? = initialGuardPos
        val visited = mutableSetOf<GuardPos>()
        while (guardPos != null) {
            if (guardPos in visited) {
                return visited.toSet() to true
            }
            visited.add(guardPos!!)
            guardPos = moveGuard(grid, guardPos!!)
        }
        return visited.toSet() to false
    }

    fun part1(input: List<String>): Int {
        val gridData = input.to2DCharArray()
        val grid = gridData.first
        val (path, _) = calculateGuardPath(grid, gridData.second)
        return path.map { it.x to it.y }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val gridData = input.to2DCharArray()
        val grid = gridData.first
        val (baseGuardPath, _) = calculateGuardPath(grid, gridData.second)
        val baseGuardPositions = baseGuardPath.map { it.x to it.y }.toSet()
        return baseGuardPositions.filter {
            grid[it.first][it.second] = 'O'
            val (_, loop) = calculateGuardPath(grid, gridData.second)
            grid[it.first][it.second] = '.'
            loop
        }.size
    }

    val testInput = readInput("Day06_test")

    check(part1(testInput) == 41)
    check(part2(testInput) == 6)


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private data class GuardPos(val x: Int, val y: Int, val direction: Direction)

private enum class Direction {
    LEFT, RIGHT, UP, DOWN;

    fun turn(): Direction {
        return when (this) {
            LEFT -> return UP
            UP -> return RIGHT
            RIGHT -> return DOWN
            DOWN -> return LEFT
        }
    }
}