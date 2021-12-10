import kotlin.system.exitProcess

fun main() {

    fun part1(input: List<String>): Int {

        var mapping = mutableMapOf<List<String>, List<String>>()
        for (s in input) {
            var (inputString, outputString) = s.split(" | ")
            var splitInput = inputString.split(" ")
            var splitoutput = outputString.split(" ")
            mapping[splitInput] = splitoutput
        }
        mapping.entries.forEach { println(it) }
        exitProcess(0)






        for (s in input) {

        }
        return 0
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part1(testInput))
//    check(part1(testInput) == 37)
//    check(part2(testInput) == 168L)

    val input = readInput("Day08")
//    println(part1(input))
//    println(part2(input))
}
