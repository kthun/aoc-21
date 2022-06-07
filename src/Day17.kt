import kotlin.math.absoluteValue

fun main() {

    class Target(val x: IntRange, val y: IntRange) {

        operator fun contains(point: Coord): Boolean {
            return point.x in x && point.y in y
        }

        fun hasMissed(point: Coord): Boolean {
            return point.x > x.last || point.y < y.first
        }
    }

    fun trajectoryPositions(velocityVector: Coord): Sequence<Coord> {
        return sequence {
            var currentPosition = Coord(0, 0)
            var currentVelocityVector = velocityVector

            while (true) {
                currentPosition = Coord(
                    currentPosition.x + currentVelocityVector.x,
                    currentPosition.y + currentVelocityVector.y

                )
                currentVelocityVector = Coord(
                    currentVelocityVector.x + if (currentVelocityVector.x > 0) -1 else if (currentVelocityVector.x < 0) 1 else 0,
                    currentVelocityVector.y - 1

                )
                yield(currentPosition)
            }
        }
    }

    fun parseInput(input: List<String>): Target {
        val targetString = input.first()
        val minX = targetString.substringAfter("=").substringBefore("..").toInt()
        val maxX = targetString.substringAfter("..").substringBefore(",").toInt()
        val minY = targetString.substringAfterLast("=").substringBeforeLast("..").toInt()
        val maxY = targetString.substringAfterLast("..").substringBeforeLast(",").toInt()

        return Target(minX..maxX, minY..maxY)
    }

    fun part1(input: List<String>): Int {
        val target = parseInput(input)

        val highestPointInTargetArc = (0..target.x.last).maxOf { x ->
            (target.y.first..target.y.first.absoluteValue).maxOf { y ->
                val track = trajectoryPositions((Coord(x, y))).takeWhile { position -> !target.hasMissed(position) }
                if (track.any { it in target }) track.maxOf { it.y } else 0
            }
        }
        return highestPointInTargetArc
    }

    fun part2(input: List<String>): Int {
        val target = parseInput(input)

        val hittingTracks = (0..target.x.last).map { x ->
            (target.y.first..target.y.first.absoluteValue).mapNotNull { y ->
                val track = trajectoryPositions((Coord(x, y))).takeWhile { position -> !target.hasMissed(position) }
                if (track.any { it in target }) Coord(x, y) else null
            }
        }.flatten()

        return hittingTracks.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}