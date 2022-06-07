fun main() {
    fun parseInput(input: List<String>): List<Int> {
        return input.map { it.toInt() }
    }

    fun part1(input: List<String>): Int {
        val part1Input = parseInput(input)
        return countDepthIncreasesEachLine(part1Input)
    }

    fun part2(input: List<String>): Int {
        val part2Input = parseInput(input)
        return countDepthIncreasesSlidingWindow(part2Input, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

private fun countDepthIncreasesSlidingWindow(depths: List<Int>, windowSize: Int): Int {
    var counter = 0
    var firstDepthsSum = 0
    for (i in 0 until windowSize) {
        firstDepthsSum += depths[i]
    }
    var prevDepthsSum = firstDepthsSum

    var newDepthsSum = 0
    for (i in 0 .. depths.lastIndex - windowSize) {
        newDepthsSum = prevDepthsSum - (depths[i]) + depths[i + windowSize]
        if (newDepthsSum > prevDepthsSum) counter++
        prevDepthsSum = newDepthsSum
    }
    return counter
}

private fun countDepthIncreasesEachLine(input: List<Int>): Int {
    var prevDepth = Int.MAX_VALUE
    var counter = 0
    for (depth in input) {
        if (depth > prevDepth) counter++
        prevDepth = depth
    }
    return counter
}
