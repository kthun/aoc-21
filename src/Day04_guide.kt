fun main() {
    fun generateBingoBoards(input: List<String>): List<BingoBoard> {
        val boardStrings = input.drop(1).chunked(6).map { board ->
            board.filter { it.isNotBlank() }
        }

        val boards = boardStrings.map { board ->
            board.map { line ->
                line.trim()
                    .split(Regex("\\W+"))
                    .map { it.toInt() }
            }
        }
        return boards.map { board -> BingoBoard.fromCollection(board) }
    }

    fun part1(input: List<String>): Int {
        val draws = input.first().split(",").map { it.toInt() }
        val bingoBoards: List<BingoBoard> = generateBingoBoards(input)

        for (draw in draws) {
            for (bingoBoard in bingoBoards) {
                bingoBoard.markNumber(draw)
                if (bingoBoard.isComplete()) {
                    return bingoBoard.unmarkedNumbers().sumOf { it } * draw
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val draws = input.first().split(",").map { it.toInt() }
        var bingoBoards: List<BingoBoard> = generateBingoBoards(input)

        for (draw in draws) {
            for (bingoBoard in bingoBoards) {
                bingoBoard.markNumber(draw)
                if (bingoBoard.isComplete()) {
                    bingoBoards = bingoBoards - bingoBoard
                    if (bingoBoards.isEmpty()) {
                        return bingoBoard.unmarkedNumbers().sumOf{ it } * draw
                    }
                }
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Cell(val value: Int, var marked: Boolean = false) {
    fun mark() {
        marked = true
    }
}

data class BingoBoard(val cells: List<List<Cell>>) {
    private val colIndices = cells[0].indices
    private val rowIndices = cells.indices

    companion object {
        fun fromCollection(coll: List<List<Int>>): BingoBoard {
            return BingoBoard(coll.map { row -> row.map { cellValue -> Cell(cellValue) }.toMutableList() })
        }
    }

    fun isComplete() = hasCompleteRow() || hasCompleteColumn()

    private fun hasCompleteRow() = cells.any { row -> row.all { it.marked } }

    private fun hasCompleteColumn(): Boolean {
        for (col in colIndices) {
            var columnOK = true
            for (row in rowIndices) {
                if (!cells[row][col].marked) {
                    columnOK = false
                    continue
                }
            }
            if (columnOK) return true
        }
        return false
    }

    fun markNumber(num: Int) {
        for (row in cells) {
            for (cell in row) {
                if (cell.value == num) {
                    cell.mark()
                    return
                }
            }
        }
    }

    fun unmarkedNumbers() = cells.flatten().filter { !it.marked }.map { it.value }
}
