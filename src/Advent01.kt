package advent2021

import java.io.File

fun main() {
    val depthMeasures = File("src/main/kotlin/advent2021/input/01.txt").readLines()
//    println(countDepthIncreasesEachLine(depthMeasures))

    println(countDepthIncreasesSlidingWindow(depthMeasures, 3))
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