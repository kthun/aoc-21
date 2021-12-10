import kotlin.math.abs

private enum class LineCountType {
    ALL_LINES,
    ORTHOGONAL_ONLY
}

fun main() {

    data class Coord(val x: Int, val y: Int)

    data class VentLine(val start: Coord, val end: Coord) {
        fun pointsCovered(typesToCount: LineCountType): List<Coord> {
            return when (typesToCount) {
                LineCountType.ALL_LINES -> pointsCovered()
                LineCountType.ORTHOGONAL_ONLY -> {
                    if (start.x != end.x && start.y != end.y) {
                        emptyList()
                    } else {
                        pointsCovered()
                    }
                }
            }
        }

        private fun pointsCovered(): List<Coord> {
            val points = mutableListOf<Coord>()
            val xDistance = start.x - end.x
            val yDistance = start.y - end.y

            val xStep = if (xDistance > 0) -1 else if (xDistance < 0) 1 else 0
            val yStep = if (yDistance > 0) -1 else if (yDistance < 0) 1 else 0

            val distance = maxOf(abs(xDistance), abs(yDistance))

            for (i in 0 .. distance) {
                points.add(Coord(start.x + i * xStep, start.y + i * yStep))
            }
            return points
        }
    }

    fun countCrossingLines(input: List<String>, typesToCount: LineCountType): Int {
        val lines = input
            .map { it.split(",", " -> ") }
            .map { VentLine(Coord(it[0].toInt(), it[1].toInt()), Coord(it[2].toInt(), it[3].toInt())) }

        val timesCovered = mutableMapOf<Coord, Int>()

        for (line in lines) {
            for (point in line.pointsCovered(typesToCount)) {
                timesCovered[point] = timesCovered.getOrDefault(point, 0) + 1
            }
        }
        return timesCovered.count { it.value >= 2 }
    }

    fun part1(input: List<String>): Int {
        return countCrossingLines(input, LineCountType.ORTHOGONAL_ONLY)
    }

    fun part2(input: List<String>): Int {
        return countCrossingLines(input, LineCountType.ALL_LINES)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
