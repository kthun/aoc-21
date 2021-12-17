import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readInputAsInts(name: String) = File("src", "$name.txt").readLines().map { it.toInt() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Coord(val x: Int, val y: Int) {
    fun orthogonalNeighbours(): List<Coord> {
        return listOf(
            Coord(x, y - 1),
            Coord(x, y + 1),
            Coord(x - 1, y),
            Coord(x + 1, y),
        )
    }
    fun neighbours(): List<Coord> {
        return orthogonalNeighbours() + listOf(
            Coord(x - 1, y - 1),
            Coord(x - 1, y + 1),
            Coord(x + 1, y - 1),
            Coord(x + 1, y + 1),
        )
    }
}