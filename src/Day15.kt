import java.util.*

private typealias ChitonCave = Array<IntArray>

private class Traversal(val point: Coord, val totalRisk: Int) : Comparable<Traversal> {
    override fun compareTo(other: Traversal) = this.totalRisk - other.totalRisk
}

private operator fun ChitonCave.get(point: Coord): Int = this[point.y][point.x]


private fun ChitonCave.getExpandedCaveRisk(point: Coord): Int {
    val gridX = point.x / this.first().size
    val gridY = point.y / this.size
    val originalRisk = this[point.y % this.size][point.x % this.first().size]
    val newRisk = originalRisk + gridX + gridY
    return (newRisk - 1) % 9 + 1
}


private fun Coord.validNeighours(cave: ChitonCave, caveExpansionFactor: Int): List<Coord> {
    return orthogonalNeighbours().filter { it.x in 0 until cave.first().size * caveExpansionFactor && it.y in 0 until cave.size * caveExpansionFactor }
}


private fun ChitonCave.findCheapestTraversalCost(
    destination: Coord = Coord(first().lastIndex, lastIndex),
    caveExpansionFactor: Int = 1
): Int {
    val toBeEvaluated = PriorityQueue<Traversal>().apply { add(Traversal(Coord(0, 0), 0)) }
    val visited = mutableSetOf<Coord>()

    while (toBeEvaluated.isNotEmpty()) {
        val current = toBeEvaluated.poll()
        if (current.point == destination) {
            return current.totalRisk
        }
        if (current.point !in visited) {
            visited.add(current.point)
            current.point.validNeighours(this, caveExpansionFactor)
                .forEach { coord -> toBeEvaluated.offer(Traversal(coord, if (caveExpansionFactor == 1) current.totalRisk + this[coord] else current.totalRisk + this.getExpandedCaveRisk(coord))) }
        }
    }
    error("Found no path!")
}


fun main() {
    fun parseInput(input: List<String>): ChitonCave =
        input.map { row ->
            row.map { char ->
                char.digitToInt()
            }.toIntArray()
        }.toTypedArray()


    fun part1(input: List<String>): Int {
        val cave = parseInput(input)
        return cave.findCheapestTraversalCost(caveExpansionFactor = 5)
    }

    fun part2(input: List<String>): Int {
        val cave = parseInput(input)
        return cave.findCheapestTraversalCost(Coord(cave.first().size * 5 - 1, cave.size * 5 - 1), caveExpansionFactor = 5)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}