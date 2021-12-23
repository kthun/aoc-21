data class Fold(val type: FoldType, val location: Int)

enum class FoldType {
    HORIZONTAL, VERTICAL
}

typealias Dot = Coord

fun main() {
    fun parseInput(input: List<String>): Pair<List<Dot>, List<Fold>> {
        val blankLineIndex = input.indexOf("")
        val dotsInput = input.subList(0, blankLineIndex)
        val foldsInput = input.subList(blankLineIndex + 1, input.size)

        val dots = dotsInput.map { it.split(",") }.map { Dot(it[0].toInt(), it[1].toInt()) }

        val folds = foldsInput.map { it.split(" ", "=") }.map {
            Fold(if (it[2] == "x") FoldType.VERTICAL else FoldType.HORIZONTAL, it[3].toInt())
        }

        return Pair(dots, folds)
    }

    fun List<Dot>.print() {
        val maxX = maxOf { it.x }
        val maxY = maxOf { it.y }
        for (i in 0..maxY) {
            for (j in 0..maxX) {
                print(
                    if (any { it.x == j && it.y == i }) "#"
                    else "."
                )
            }
            println()
        }
    }

    fun List<Dot>.fold(fold: Fold): List<Dot> {
        val foldType = fold.type
        val foldLocation = fold.location

        return this.map { dot ->
            when (foldType) {
                FoldType.HORIZONTAL -> {
                    if (dot.y < foldLocation) dot
                    else Dot(dot.x, foldLocation - (dot.y - foldLocation))
                }
                FoldType.VERTICAL -> {
                    if (dot.x < foldLocation) dot
                    else Dot(foldLocation - (dot.x - foldLocation), dot.y)
                }
            }
        }.toList()
            .distinct()
    }

    fun part1(input: List<String>): Int {
        var (dots, folds) = parseInput(input)
        val fold = folds[0]
        dots = dots.fold(fold)

        return dots.size
    }

    fun part2(input: List<String>): Int {
        var (dots, folds) = parseInput(input)
        for (fold in folds) {
            dots = dots.fold(fold)
        }

        dots.print()
        return dots.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    println(part2(testInput))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}