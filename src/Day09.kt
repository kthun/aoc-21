data class MapCell(val coord: Coord, val height: Int)

fun main() {

    fun part1(input: List<String>): Int {
        val heightMapCharsList = input.map { it.toCharArray() }

        val topoMap = input.mapIndexed { rowIndex, line ->
            line.toCharArray()
                .mapIndexed { colIndex, char ->
                    MapCell(Coord(rowIndex, colIndex), char - '0')
                }
        }

        val maxX = topoMap.lastIndex
        val maxY = topoMap[0].lastIndex
        var sumRiskPoints = 0

        for (cell in topoMap.flatten()) {
            val minNeighbourHeight = cell.coord
                .neighbours()
                .filter { neighbour ->
                    neighbour.x in 0..maxX && neighbour.y in 0..maxY
                }
                .map { neighbourCoord ->
                    topoMap[neighbourCoord.x][neighbourCoord.y].height
                }
                .minOf { it }
            if (cell.height < minNeighbourHeight) {
                sumRiskPoints += 1 + cell.height
            }
        }

        return sumRiskPoints
    }


    fun part2(input: List<String>): Long {
        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println(part1(testInput))
    check(part1(testInput) == 15)
//    check(part2(testInput) == 168L)

    val input = readInput("Day09")
    println(part1(input))
//    println(part2(input))
}
