private fun String.generateNextStage(insertionRules: Map<Pair<Char, Char>, Char>): String {
    return zipWithNext()
        .joinToString("") { (a, b) -> "" + a + insertionRules.getOrDefault(Pair(a, b), "") } + last()
}

fun main() {
    fun part1(input: List<String>): Int {
        val template = input[0]
        val insertionRules =
            input
                .drop(2)
                .map { it.split(" -> ") }
                .associate { (input , output) -> Pair(input[0], input[1]) to output.single() }

        var polymer = template
        repeat (10) {
            polymer = polymer.generateNextStage(insertionRules)
        }
        val elementOccurences = polymer.groupBy { it }
            .mapValues { it.value.size }
        return elementOccurences.maxOf { it.value } - elementOccurences.minOf { it.value }
    }

    fun calculateNextPairsMap(
        pairsMap: Map<Pair<Char, Char>, Long>,
        insertionRules: Map<Pair<Char, Char>, Char>
    ): Map<Pair<Char, Char>, Long> {
        var newPairsMap = mutableMapOf<Pair<Char, Char>, Long>()

        pairsMap.map { (key, value) ->
            val inserted = insertionRules[key]!!
            val pair1 = Pair(key.first, inserted)
            val pair2 = Pair(inserted, key.second)

            newPairsMap[pair1] = newPairsMap.getOrDefault(pair1, 0L) + value
            newPairsMap[pair2] = newPairsMap.getOrDefault(pair2, 0L) + value
        }
        return newPairsMap
    }

    fun part2(input: List<String>): Long {
        val template = input[0]
        val insertionRules =
            input
                .drop(2)
                .map { it.split(" -> ") }
                .associate { (input , output) -> Pair(input[0], input[1]) to output.single() }

        var pairsMap =
            template
                .zipWithNext()
                .groupBy { it }
                .mapValues { it.value.size.toLong() }

        repeat (40) {
            pairsMap = calculateNextPairsMap(pairsMap, insertionRules)
        }

        var elementOccurencesDoubleCounted = mutableMapOf<Char, Long>()

        for ((pair, occurences) in pairsMap) {
            elementOccurencesDoubleCounted[pair.first] = elementOccurencesDoubleCounted.getOrDefault(pair.first, 0L) + occurences
            elementOccurencesDoubleCounted[pair.second] = elementOccurencesDoubleCounted.getOrDefault(pair.second, 0L) + occurences
        }
        elementOccurencesDoubleCounted[template[0]] = elementOccurencesDoubleCounted.getOrDefault(template[0], 0L) + 1
        elementOccurencesDoubleCounted[template.last()] = elementOccurencesDoubleCounted.getOrDefault(template.last(), 0L) + 1

        val elementOccurences = elementOccurencesDoubleCounted.map {
                (key, value) -> key to value / 2
        }.toMap()

        println(elementOccurences)

        return elementOccurences.maxOf { it.value } - elementOccurences.minOf { it.value }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

