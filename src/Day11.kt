data class Octopus(
    val coord: Coord,
    var level: Int,
    val flashedThisTurn: Boolean = false,
    val neighbourCoords: List<Coord> = coord.neighbours()
)

fun main() {
    fun printGrid(octopuses: List<List<Octopus>>) {
        for (row in octopuses) {
            for (octopus in row) {
                print(octopus.level)
            }
            println()
        }
        println()
    }

    fun List<List<Octopus>>.propogateFlashAndReturnFlashers(): List<Octopus> {
        val octopusList = this.flatten()
        val width = this[0].size
        val height = this.size

        val readyToFlash = octopusList.filter { it.level > 9 }
        readyToFlash.forEach { it.level = 0 }

        readyToFlash.flatMap { it.neighbourCoords }
            .filter { it.x in 0 until width && it.y in 0 until height }
            .map { this[it.x][it.y] }
            .filter { it in octopusList && it.level != 0 }
            .forEach { it.level++ }
        return readyToFlash
    }

    fun List<List<Octopus>>.advanceOneTurnAndCountFlashes(): Int {
        val octopusList = flatten()
        var numFlashesThisTurn = 0
        octopusList.forEach { it.level++ }
        do {
            val flashersThisTurn = propogateFlashAndReturnFlashers()
        } while (flashersThisTurn.isNotEmpty())
        numFlashesThisTurn += octopusList.count { it.level == 0 }
        return numFlashesThisTurn
    }

    fun List<String>.buildOctopusGrid() = withIndex().map { (x, line) ->
        line.withIndex().map { (y, char) ->
            Octopus(Coord(x, y), char.digitToInt())
        }
    }

    fun part1(input: List<String>): Int {
        val octopusGrid = input.buildOctopusGrid()

        var flashCounter = 0
        for (turn in 1..100) {
            flashCounter += octopusGrid.advanceOneTurnAndCountFlashes()
        }
        return flashCounter
    }

    fun part2(input: List<String>): Int {
        val octopusGrid = input.buildOctopusGrid()
        val numOctopuses = octopusGrid.sumOf { it.size }

        for (i in 1..1000) {
            val flashersThisTurn = octopusGrid.advanceOneTurnAndCountFlashes()
            if (flashersThisTurn == numOctopuses) {
                return i
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
