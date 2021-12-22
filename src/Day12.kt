enum class CAVESIZES {
    SMALL, LARGE
}

data class Cave(val name: String, val size: CAVESIZES) {
    companion object {
        fun createCave(name: String): Cave {
            return Cave(name, if (name.all(Char::isUpperCase)) CAVESIZES.LARGE else CAVESIZES.SMALL)
        }
    }
}

fun visitRulePart1(cave: Cave, path: List<Cave>): Boolean {
    if (cave !in path) return true
    return cave.size == CAVESIZES.LARGE
}

fun visitRulePart2(cave: Cave, path: List<Cave>): Boolean {
    if (cave.size == CAVESIZES.LARGE) return true
    if (cave.name in setOf("start", "end")) return cave !in path
    if (path
            .groupBy { it }
            .filter { it.key.size == CAVESIZES.SMALL }
            .any { it.value.size > 1}) return cave !in path
    return true
}

fun main() {
    fun parseInput(input: List<String>): Map<Cave, List<Cave>> {
        val caveNeighbours = input
            .map { it.split("-") }
            .flatMap {
                listOf(
                    Cave.createCave(it.first()) to Cave.createCave(it.last()),
                    Cave.createCave(it.last()) to Cave.createCave(it.first())
                )
            }
            .groupBy({ it.first }, { it.second })

        return caveNeighbours
    }

    fun part1(input: List<String>): Int {
        val cavesWithNeighbours = parseInput(input)

        val startCave = cavesWithNeighbours.keys.first { it.name == "start" }
        val endCave = cavesWithNeighbours.keys.first { it.name == "end" }

        fun traverse(
            allowedToVisit: (Cave, List<Cave>) -> Boolean,
            path: List<Cave> = listOf(startCave)
        ): List<List<Cave>> {
            return if (path.last() == endCave) listOf(path)
            else cavesWithNeighbours.getValue(path.last())
                .filter { allowedToVisit(it, path) }
                .flatMap {
                    traverse(allowedToVisit, path + it)
                }
        }
        return traverse(::visitRulePart1).size
    }


    fun part2(input: List<String>): Int {
        val cavesWithNeighbours = parseInput(input)

        val startCave = cavesWithNeighbours.keys.first { it.name == "start" }
        val endCave = cavesWithNeighbours.keys.first { it.name == "end" }

        fun traverse(
            allowedToVisit: (Cave, List<Cave>) -> Boolean,
            path: List<Cave> = listOf(startCave)
        ): List<List<Cave>> {
            return if (path.last() == endCave) listOf(path)
            else cavesWithNeighbours.getValue(path.last())
                .filter { allowedToVisit(it, path) }
                .flatMap {
                    traverse(allowedToVisit, path + it)
                }
        }

        return traverse(::visitRulePart2).size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)
//    println(part2(testInput))
//    check(part2(testInput) == 5)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}


