fun main() {
    fun part1(input: List<String>): Int {
        return calculatePosSimple(input)
    }

    fun part2(input: List<String>): Int {
        return calculatePosAim(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun calculatePosAim(instructions: List<String>): Int {
    var currentHorPos = 0
    var currentDepth = 0
    var currentVertAim = 0

    for (instruction in instructions) {
        val split = instruction.split(" ")

        when (split[0]) {
            "forward" -> {
                currentHorPos += split[1].toInt()
                currentDepth += split[1].toInt() * currentVertAim
            }
            "down" -> currentVertAim += split[1].toInt()
            "up" -> currentVertAim -= split[1].toInt()
        }
    }
    return currentHorPos * currentDepth
}

private fun calculatePosSimple(instructions: List<String>): Int {
    var currentHorPos = 0
    var currentDepth = 0

    for (instruction in instructions) {
        val split = instruction.split(" ")

        when (split[0]) {
            "forward" -> currentHorPos += split[1].toInt()
            "down" -> currentDepth += split[1].toInt()
            "up" -> currentDepth -= split[1].toInt()
        }
    }
    return currentHorPos * currentDepth
}
