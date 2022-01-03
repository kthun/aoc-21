data class MapCell(val coord: Coord, val height: Int)

fun main() {

    fun MapCell.validNeighbours(topoMap: List<List<MapCell>>): List<MapCell> =
        coord.orthogonalNeighbours().mapNotNull { coord ->
            topoMap.getOrNull(coord.x)?.getOrNull(coord.y)
        }

    fun MapCell.getBasinOfLowPoint(topoMap: List<List<MapCell>>): Set<MapCell> {
        val basin = mutableSetOf(this)
        val edgeCells = mutableListOf(this)

        while (edgeCells.isNotEmpty()) {
            val newNeighbours = edgeCells.removeFirst()
                .validNeighbours(topoMap)
                .filter { it !in basin }
                .filter { it.height != 9 }
            basin.addAll(newNeighbours)
            edgeCells.addAll(newNeighbours)
        }
        return basin
    }

    fun parseInput(input: List<String>) = input.mapIndexed { rowIndex, line ->
        line.toCharArray()
            .mapIndexed { colIndex, char ->
                MapCell(Coord(rowIndex, colIndex), char - '0')
            }
    }

    fun part1(input: List<String>): Int {
        val topoMap = parseInput(input)

        val maxX = topoMap.lastIndex
        val maxY = topoMap[0].lastIndex
        var sumRiskPoints = 0

        for (cell in topoMap.flatten()) {
            val minNeighbourHeight = cell
                .validNeighbours(topoMap)
                .minOf { it.height }
            if (cell.height < minNeighbourHeight) {
                sumRiskPoints += 1 + cell.height
            }
        }
        return sumRiskPoints
    }


    fun part2(input: List<String>): Int {
        val topoMap = parseInput(input)

        val lowPoints = topoMap.flatten()
            .filter { cell ->
                cell.validNeighbours(topoMap).none { neighbour -> neighbour.height < cell.height }
            }

        val basins = lowPoints.map { it.getBasinOfLowPoint(topoMap) }
        val threeLargestBasins = basins.sortedByDescending { it.size }.take(3)
        return threeLargestBasins.map { it.size }.reduce(Int::times)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    println(part1(testInput))
//    println(part2(testInput))
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
