fun main() {

    fun inputToMapping(input: List<String>): MutableMap<List<String>, List<String>> {
        var mapping = mutableMapOf<List<String>, List<String>>()
        for (s in input) {
            var (inputString, outputString) = s.split(" | ")
            var splitInput = inputString.split(" ").map { it.toList().sorted().joinToString("") }
            var splitoutput = outputString.split(" ").map { it.toList().sorted().joinToString("") }
            mapping[splitInput] = splitoutput
        }
        return mapping
    }

    fun part1(input: List<String>): Int {
        val mapping = inputToMapping(input)

        return mapping
            .values
            .toList()
            .flatMap { it.toList() }
            .count { it.length in setOf(2, 3, 4, 7) }
    }

    fun part2(input: List<String>): Int {
        val mapping = inputToMapping(input)

        val segmentsNumberMap = mapOf(
            "cf" to '1',
            "acdeg" to '2',
            "acdfg" to '3',
            "bcdf" to '4',
            "abdfg" to '5',
            "abdefg" to '6',
            "acf" to '7',
            "abcdefg" to '8',
            "abcdfg" to '9',
            "abcefg" to '0'
        )

        var sum = 0
        for ((inputWires, digitsToDecode) in mapping) {
            var segmentsToWiresMap = mutableMapOf<Char, Char>()

            val oneWires = inputWires
                .find { it.length == 2 }!!
                .toCharArray()

            val sevenWires = inputWires
                .find { it.length == 3 }!!
                .toCharArray()

            val fourWires = inputWires
                .find { it.length == 4 }!!
                .toCharArray()

            val eightWires = inputWires
                .find { it.length == 7 }!!
                .toCharArray()

            val fiveLengthNumbersWireCount = inputWires
                .filter { it.length == 5 }
                .flatMap { it.toCharArray().toList() }
                .groupBy { it }
                .mapValues { it.value.count() }

            val sixLengthNumbersWireCount = inputWires
                .filter { it.length == 6 }
                .flatMap { it.toCharArray().toList() }
                .groupBy { it }
                .mapValues { it.value.count() }

            segmentsToWiresMap['a'] = sevenWires.first { it !in oneWires }

            segmentsToWiresMap['b'] = fiveLengthNumbersWireCount
                .filter { it.value == 1 }
                .map { it.key }
                .first { it in fourWires }

            segmentsToWiresMap['c'] = sixLengthNumbersWireCount
                .filter { it.value == 2 }
                .map { it.key }
                .first { it in oneWires }

            segmentsToWiresMap['d'] = fiveLengthNumbersWireCount
                .filter { it.value == 3 }
                .map { it.key }
                .first { it in fourWires }

            segmentsToWiresMap['e'] = fiveLengthNumbersWireCount
                .filter { it.value == 1 }
                .map { it.key}
                .first { it !in fourWires }

            segmentsToWiresMap['f'] = sixLengthNumbersWireCount
                .filter { it.value == 3 }
                .map { it.key }
                .first { it in oneWires }

            segmentsToWiresMap['g'] = fiveLengthNumbersWireCount
                .filter { it.value == 3 }
                .map { it.key }
                .filter { it !in fourWires }
                .first { it !in sevenWires }

//            Number    Segments
//            2         5       A   C D E   G
//            3         5       A   C D   F G
//            5         5       A B   D   F G

//            0         6       A B C   E F G
//            6         6       A B   D E F G
//            9         6       A B C D   F G

//            1         2           C     F
//            4         4         B C D   F
//            7         3       A   C     F
//            8         7       A B C D E F G

            val wiresToSegmentsMap = segmentsToWiresMap.entries.associate { (key, value) -> value to key }

            var decodedNumberString = ""
            for (digitString in digitsToDecode) {
                var outputWires = mutableListOf<Char>()
                for (c in digitString) {
                    wiresToSegmentsMap[c]?.let { outputWires.add(it) }
                }
                val outputWiresString = outputWires.sorted().joinToString("")
                var thisDigit = segmentsNumberMap[outputWiresString]!!
                decodedNumberString += thisDigit
            }
            sum += decodedNumberString.toInt()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
