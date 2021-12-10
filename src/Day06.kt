fun main() {

    data class Fish(val age: Int)

    fun ageFish(ageMap: MutableMap<Int, Long>, daysToAge: Int): MutableMap<Int, Long> {
        for (i in 1..daysToAge) {
            val fishGivingBirth = ageMap.getValue(0)
            for (i in 1..8) {
                ageMap[i - 1] = ageMap.getValue(i)
            }
            ageMap[6] = ageMap.getValue(6) + fishGivingBirth
            ageMap[8] = fishGivingBirth
        }
        return ageMap
    }

    fun getFishCount(input: List<String>, daysToAge: Int): Long {
        var initialFishList = input[0]
            .split(",")
            .map { it.toInt() }
            .map(::Fish)

        var initialAgeMap = initialFishList
            .groupingBy { it.age }
            .eachCount()
            .mapValues { (_, value) -> value.toLong() }
            .toMutableMap()

        for (i in 0..8) {
            initialAgeMap[i] = initialAgeMap.getOrDefault(i, 0)
        }
        val agedFishMap = ageFish(initialAgeMap, daysToAge)

        return agedFishMap.values.sum()
    }

    fun part1(input: List<String>): Long {
        return getFishCount(input, 80)
    }

    fun part2(input: List<String>): Long {
        return getFishCount(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
