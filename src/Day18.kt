import kotlin.math.ceil
import kotlin.math.floor

private sealed class SnailfishNumber {
    var parent: SnailfishNumber? = null
    abstract fun magnitude(): Int
    abstract fun split(): Boolean
    abstract fun regularsInOrder(): List<RegularNumber>
    abstract fun pairsInOrderWithDepth(depth: Int = 0): List<PairNumberDepth>

    private fun root(): SnailfishNumber =
        if (parent == null) this else parent!!.root()

    private fun explode(): Boolean {
        val pairs = root().pairsInOrderWithDepth()
        val explodingPair = pairs.firstOrNull { it.depth == 4 }?.pair
        if (explodingPair != null) {
            val regulars = root().regularsInOrder()
            val leftExploderIndex = regulars.indexOfFirst { it === explodingPair.left }
            val leftNeighbour = regulars.elementAtOrNull(leftExploderIndex - 1)
            val rightNeighbour = regulars.elementAtOrNull(leftExploderIndex + 2)
            leftNeighbour?.addValue(explodingPair.left as RegularNumber)
            rightNeighbour?.addValue(explodingPair.right as RegularNumber)
            (explodingPair.parent as PairNumber).childHasExploded(explodingPair)
            return true
        }
        return false
    }

    fun reduce() {
        do {
            val stateChanged = explode() || split()
        } while (stateChanged)
    }

    operator fun plus(other: SnailfishNumber): SnailfishNumber =
        PairNumber(this, other).apply { reduce() }


    data class RegularNumber(var value: Int) : SnailfishNumber() {
        override fun magnitude(): Int = value
        override fun split(): Boolean = false
        override fun regularsInOrder(): List<RegularNumber> = listOf(this)
        override fun pairsInOrderWithDepth(depth: Int): List<PairNumberDepth> = emptyList()

        fun addValue(added: RegularNumber) {
            value += added.value
        }

        fun splitToPair(parent: PairNumber): PairNumber {
            val leftChild = RegularNumber(floor(value.toDouble() / 2.0).toInt())
            val rightChild = RegularNumber(ceil(value.toDouble() / 2.0).toInt())
            val splitNumber = PairNumber(leftChild, rightChild)
            splitNumber.parent = parent
            return splitNumber
        }
    }

    data class PairNumber(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber() {
        init {
            left.parent = this
            right.parent = this
        }

        override fun magnitude() = left.magnitude() * 3 + right.magnitude() * 2

        override fun split(): Boolean {
            if (left is RegularNumber) {
                val regularLeft = left as RegularNumber
                if (regularLeft.value >= 10) {
                    left = regularLeft.splitToPair(this)
                    return true
                }
            }
            val splitInLeftSubtree = left.split()
            if (splitInLeftSubtree) return true
            if (right is RegularNumber) {
                val regularRight = right as RegularNumber
                if (regularRight.value >= 10) {
                    right = regularRight.splitToPair(this)
                    return true
                }
            }
            return right.split()
        }

        override fun regularsInOrder(): List<RegularNumber> = left.regularsInOrder() + right.regularsInOrder()
        override fun pairsInOrderWithDepth(depth: Int): List<PairNumberDepth> =
            left.pairsInOrderWithDepth(depth + 1) + listOf(PairNumberDepth(depth, this)) + right.pairsInOrderWithDepth(
                depth + 1
            )

        fun childHasExploded(explodedChild: PairNumber) {
            val newChild = RegularNumber(0)
            newChild.parent = this
            when {
                this.left === explodedChild -> this.left = newChild
                this.right === explodedChild -> this.right = newChild
            }
        }
    }

    companion object {
        fun of(input: String): SnailfishNumber {
            val stack = mutableListOf<SnailfishNumber>()
            input.forEach { char ->
                when {
                    char.isDigit() -> stack.add(RegularNumber(char.digitToInt()))
                    char == ']' -> {
                        val right = stack.removeLast()
                        val left = stack.removeLast()
                        stack.add(PairNumber(left, right))
                    }
                }
            }
            return stack.removeFirst()
        }
    }

    data class PairNumberDepth(val depth: Int, val pair: PairNumber)
}


fun main() {

    fun part1(input: List<String>): Int {
        val numbers = input.map { line -> SnailfishNumber.of(line) }
        return numbers.reduce { a, b -> a + b }.magnitude()
    }


    fun part2(input: List<String>): Int =
        input.mapIndexed { index, left ->
            input.drop(index + 1).map { right ->
                listOf(
                    SnailfishNumber.of(left) to SnailfishNumber.of(right),
                    SnailfishNumber.of(right) to SnailfishNumber.of(left)
                )
            }.flatten()
        }.flatten()
            .maxOf { (it.first + it.second).magnitude() }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}