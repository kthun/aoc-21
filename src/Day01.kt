fun main() {
    fun part1(input: List<String>): Int {
        return countDepthIncreasesEachLine(input)
    }

    fun part2(input: List<String>): Int {
        return countDepthIncreasesSlidingWindow(input, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

private fun countDepthIncreasesSlidingWindow(lines: List<String>, windowSize: Int): Int {
    val depthMeasures = lines.asSequence().map { it.toInt() }.toList()
    var counter = 0

    var firstDepthsSum = 0
    for (i in 0 until windowSize) {
        firstDepthsSum += depthMeasures[i]
    }
    var prevDepthsSum = firstDepthsSum

    var newDepthsSum = 0
    for (i in 0 .. depthMeasures.lastIndex - windowSize) {
        newDepthsSum = prevDepthsSum - (depthMeasures[i]) + depthMeasures[i + windowSize]
        if (newDepthsSum > prevDepthsSum) counter++
        prevDepthsSum = newDepthsSum
    }
    return counter
}

private fun countDepthIncreasesEachLine(lines: List<String>): Int {
    var prevDepth = Int.MAX_VALUE
    var counter = 0
    for (line in lines) {
        val newDepth = line.toInt()
        if (newDepth > prevDepth) counter++
        prevDepth = newDepth
    }
    return counter
}
