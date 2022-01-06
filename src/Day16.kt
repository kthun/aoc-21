enum class PacketType(val id: Int) {
    SUM(0),
    PRODUCT(1),
    MINIMUM(2),
    MAXIMUM(3),
    LITERAL_VALUE(4),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUAL_TO(7)
}

fun Iterator<Char>.nextBitChunk(numBits: Int): String =
    (1..numBits).map { next() }.joinToString("")

fun Iterator<Char>.nextIntFromBitChunk(numBits: Int): Int =
    nextBitChunk(numBits).toInt(2)

fun Iterator<Char>.nextBitChunksUntilStopCondition(numBits: Int, stopCondition: (String) -> Boolean): List<String> {
    val output = mutableListOf<String>()
    do {
        val bitChunk = this.nextBitChunk(numBits)
        output.add(bitChunk)
    } while (!stopCondition(bitChunk))
    return output
}

fun <T> Iterator<Char>.executeUntilEmpty(function: (Iterator<Char>) -> T): List<T> {
    val output = mutableListOf<T>()
    while (this.hasNext()) {
        output.add(function(this))
    }
    return output
}

private sealed class BITSPacket(val version: Int) {
    abstract val value: Long

    companion object {
        fun of(input: Iterator<Char>): BITSPacket {
            val version = input.nextIntFromBitChunk(3)
            val packetTypeId = input.nextIntFromBitChunk(3)
            val packetType: PacketType? = PacketType.values().find { it.id == packetTypeId }
//            val packetType = input.nextIntFromBitChunk(3)
            return when (packetType) {
                null -> error("Illegal packet type")
                PacketType.LITERAL_VALUE -> BITSLiteral.of(version, input)
                else -> BITSOperator.of(version, packetType, input)
            }
        }
    }

    abstract fun allVersions(): List<Int>
}

private class BITSLiteral(version: Int, override val value: Long) : BITSPacket(version) {
    override fun allVersions(): List<Int> {
        return listOf(version)
    }

    companion object {
        fun of(version: Int, input: Iterator<Char>): BITSLiteral {
            return BITSLiteral(version, parseLiteralValue(input))
        }

        private fun parseLiteralValue(input: Iterator<Char>): Long {
            return input
                .nextBitChunksUntilStopCondition(5) { it.startsWith('0') }
                .joinToString("") { it.drop(1) }
                .toLong(2)
        }
    }
}

private class BITSOperator(version: Int, packetType: PacketType, val subPackets: List<BITSPacket>): BITSPacket(version) {
    override fun allVersions(): List<Int> {
        return listOf(version) + subPackets.flatMap { it.allVersions() }
    }

    override val value: Long = when (packetType) {
        PacketType.SUM -> subPackets.sumOf { it.value }
        PacketType.PRODUCT -> subPackets.fold(1) { acc, next -> acc * next.value }
        PacketType.MINIMUM -> subPackets.minOf { it.value }
        PacketType.MAXIMUM -> subPackets.maxOf { it.value }
        PacketType.GREATER_THAN -> if (subPackets.first().value > subPackets.last().value) 1 else 0
        PacketType.LESS_THAN -> if (subPackets.first().value < subPackets.last().value) 1 else 0
        PacketType.EQUAL_TO -> if (subPackets.first().value == subPackets.last().value) 1 else 0
        else -> error("Unkown packet type!")
    }

    companion object {
        fun of(version: Int, packetType: PacketType, input: Iterator<Char>): BITSOperator {
            return when (input.nextIntFromBitChunk(1)) {
                0 -> {
                    val subPacketLength = input.nextIntFromBitChunk(15)
                    val subPacketIterator = input.nextBitChunk(subPacketLength).iterator()
                    val subPackets = subPacketIterator.executeUntilEmpty { of(it) }
                    BITSOperator(version, packetType, subPackets)
                }
                1 -> {
                    val numSubPackets = input.nextIntFromBitChunk(11)
                    val subPackets = (1..numSubPackets).map { of(input) }
                    BITSOperator(version, packetType, subPackets)
                }
                else -> error("Illegal value for input length type")
            }
        }
    }
}

fun main() {
       fun part1(input: List<String>): Int {
        val hexString = input.first()
        val charList = hexString.map { it.digitToInt(16).toString(2).padStart(4, '0') }.flatMap { it.toList() }
        val charIterator = charList.iterator()

        return BITSPacket.of(charIterator).allVersions().sum()
    }

    fun part2(input: List<String>): Long {
        val hexString = input.first()
        val charList = hexString.map { it.digitToInt(16).toString(2).padStart(4, '0') }.flatMap { it.toList() }
        val charIterator = charList.iterator()

        return BITSPacket.of(charIterator).value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 54L)
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}