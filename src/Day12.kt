public enum class CAVESIZES {
    SMALL, LARGE
}

data class Cave(val name: String, val size: CAVESIZES, var neighbours: MutableList<Cave> = mutableListOf<Cave>()) {
    companion object {
        fun createCave(name: String): Cave {
            return Cave(name, if (name.all(Char::isUpperCase)) CAVESIZES.LARGE else CAVESIZES.SMALL)
        }
    }
}

data class Link(val from: Cave, val to: Cave)

data class Path(val visitedCaves: MutableList<Cave>) {
    fun extendPathTo(nextCave: Cave): Path? {
        return if (nextCave.size == CAVESIZES.SMALL && visitedCaves.contains(nextCave)) {
            null
        } else {
            val extendedPath = Path(this.visitedCaves)
            extendedPath.visitedCaves.add(nextCave)
            extendedPath
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {

        var caves = mutableMapOf<String, Cave>()
        fun getOrCreateCave(caveName: String): Cave {
            return if (caves.containsKey(caveName)) {
                caves[caveName]!!
            } else {
                val newCave = Cave.createCave(caveName)
                caves[caveName] = newCave
                newCave
            }
        }

        val inputRegex = """([A-Za-z]+)-([A-Za-z]+)""".toRegex()
        input.forEach { line ->
            val (caveFromString, caveToString) = inputRegex.matchEntire(line)?.destructured
                ?: throw IllegalArgumentException("")
            val caveFrom = getOrCreateCave(caveFromString)
            val caveTo = getOrCreateCave(caveToString)
            caveFrom.neighbours.add(caveTo)
        }

        val pathsToCave = mutableMapOf<Cave, MutableList<Path>>()
        for (cave in caves.values) {
            pathsToCave[cave] = mutableListOf<Path>()
        }

        pathsToCave[caves["start"]!!]!!.add(Path(mutableListOf()))
        val currentCave = caves["start"]!!


        val pathsToCurrentCave = pathsToCave[currentCave]!!
        for (path in pathsToCurrentCave) {
            for (neighbour in currentCave.neighbours) {
                val extendedPath = path.extendPathTo(neighbour)
                extendedPath?.let { pathsToCave[neighbour]!!.add(it) }
            }
        }



//        println(caves)
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println(part1(testInput))
//    check(part1(testInput) == 7)
//    check(part2(testInput) == 5)

    val input = readInput("Day12")
//    println(part1(input))
//    println(part2(input))
}


