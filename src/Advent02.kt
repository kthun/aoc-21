package advent2021

import java.io.File

fun main() {

    val instructions = File("src/main/kotlin/advent2021/input/02.txt").readLines()

    val simple = calculatePosSimple(instructions)
    println(simple[0] * simple[1])

    val aim = calculatePosAim(instructions)
    println(aim[0] * aim[1])

}

private fun calculatePosAim(instructions: List<String>): List<Int> {
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
    return listOf(currentHorPos, currentDepth)
}

private fun calculatePosSimple(instructions: List<String>): List<Int> {
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
    return listOf(currentHorPos, currentDepth)
}