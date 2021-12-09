import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val gammaDelta = mostLeastCommonBit(input)
        return gammaDelta[0] * gammaDelta[1]
    }

    fun part2(input: List<String>): Int {
        val o2rating = findAirRating(input.toMutableList(), true)
        val co2rating = findAirRating(input.toMutableList(), false)
        return o2rating * co2rating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun findAirRating(lines: List<String>, findMostCommon: Boolean): Int {

    var myLines = lines
    var bitIndexToCheck = 0

    while (myLines.size > 1) {
        val bitArrayCounters = createBitArrayCounters(myLines)

        val bitToEliminate = if (bitArrayCounters[bitIndexToCheck][0] == bitArrayCounters[bitIndexToCheck][1]) {
            if (findMostCommon) 0 else 1
        } else if (bitArrayCounters[bitIndexToCheck][0] > bitArrayCounters[bitIndexToCheck][1]) {
            if (findMostCommon) 1 else 0
        } else {
            if (findMostCommon) 0 else 1
        }

        myLines = myLines.asSequence().filter { it[bitIndexToCheck].digitToInt() != bitToEliminate }.toMutableList()
        bitIndexToCheck++
    }
    return convertBinaryToDecimal(myLines[0].toLong())
}

fun mostLeastCommonBit(lines: List<String>): List<Int> {

    val lineEnd = lines[0].lastIndex
    val bitArrayCounters = createBitArrayCounters(lines)

    var mostCommonString = ""
    for (i in 0..lineEnd) {
        if (bitArrayCounters[i][0] > bitArrayCounters[i][1]) {
            mostCommonString += '0'
        } else {
            mostCommonString += '1'
        }
    }

    var leastCommonString = ""
    for (c in mostCommonString) {
        if (c == '0') {
            leastCommonString += '1'
        } else {
            leastCommonString += '0'
        }
    }

    val decMost = convertBinaryToDecimal(mostCommonString.toLong())
    val decLeast = convertBinaryToDecimal(leastCommonString.toLong())

    return listOf(decMost, decLeast)
}

private fun createBitArrayCounters(lines: List<String>): List<IntArray> {
    val lineEnd = lines[0].lastIndex
    val bitArrayCounters = mutableListOf<IntArray>()

    for (i in 0..lineEnd) {
        bitArrayCounters.add(intArrayOf(0, 0))
    }

    for (line in lines) {
        for ((i: Int, c) in line.withIndex()) {
            val binDigit = c.digitToInt()
            bitArrayCounters[i][binDigit]++
        }
    }
    return bitArrayCounters
}

fun convertBinaryToDecimal(binaryNum: Long): Int {
    var num = binaryNum
    var decimalNumber = 0
    var i = 0
    var remainder: Long

    while (num.toInt() != 0) {
        remainder = num % 10
        num /= 10
        decimalNumber += (remainder * 2.0.pow(i.toDouble())).toInt()
        ++i
    }
    return decimalNumber
}
