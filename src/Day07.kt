import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        var positions = input[0].split(",").map { it.toInt() }.sorted()
        val median = positions[positions.size / 2]
        return positions.sumOf { abs(it - median) }
    }

    fun part2(input: List<String>): Long {
        var positions = input[0].split(",").map { it.toInt() }.sorted()

        var cheapestMove = Long.MAX_VALUE
        for (moveTo in positions.first()..positions.last()) {
            var sumCost = 0
            for (p in positions) {
                val distance = abs(p - moveTo)
                sumCost += distance * (distance + 1) / 2
            }
            if (sumCost < cheapestMove) {
                cheapestMove = sumCost.toLong()
            }
        }
        return cheapestMove
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
