import kotlin.system.exitProcess

//import Utils.Coord

data class Octopus(val coord: Coord, var level: Int, val flashedThisTurn: Boolean = false, val neighbourCoords: List<Coord> = coord.neighbours()) {


//    fun nextLevelAndMaybeFlash(turn: Int): Boolean {
//        if (lastFlash < turn) {
//            level++
//            if (level >= 10) {
//                level == 0
//                neighbours.forEach { it.nextLevelAndMaybeFlash(turn) }
//                return true
//            }
//        }
//        return false
//    }
}

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

    fun part1(input: List<String>): Int {
        val octopusGrid = input.withIndex().map { (x, line) ->
            line.withIndex().map { (y, char) ->
                Octopus(Coord(x, y), char.digitToInt())
            }
        }

        var flashCounter = 0
        val width = octopusGrid[0].size
        val height = octopusGrid.size

        printGrid(octopusGrid)
        val octopusList = octopusGrid.flatten()

        for (turn in 1..5) {
            octopusList.forEach { it.level++ }


            do {
                val readyToFlash = octopusList.filter { it.level > 9 }
//                println("numflashers: ${readyToFlash.size}")
                readyToFlash.forEach { it.level = 0 }

                readyToFlash.flatMap { it.neighbourCoords }
                    .filter { it.x in 0 until width && it.y in 0 until height }
                    .map { octopusGrid[it.x][it.y] }
                    .filter { it in octopusList && it.level != 0 }
                    .forEach { it.level++ }
            } while (readyToFlash.isNotEmpty())

            printGrid(octopusGrid)

            flashCounter += octopusList.count { it.level == 0 }



        }
        return flashCounter
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println(part1(testInput))
//    check(part1(testInput) == 1)

    val input = readInput("Day11")
//    println(part1(input))
//    println(part2(input))
}
