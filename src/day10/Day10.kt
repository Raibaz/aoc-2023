import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

enum class Paths(val value: Char) {
    PIPE('|'),
    DASH('-'),
    SEVEN('7'),
    F('F'),
    J('J'),
    L('L'),
    DOT('.'),
    START('S')
}

class Cell(val row: Int, val col: Int, val value: Paths)

class Day10Grid(val rows: List<List<Cell>>) {

    fun log() {
        rows.forEach { row ->
            row.map { it.value.value }.joinToString("").println()
        }
    }
    fun findStart(): Cell? {
        rows.forEach { row ->
            row.forEach {
                if (it.value == Paths.START) {
                    return it
                }
            }
        }
        return null
    }

    fun findNext(current: Cell, from: Cell): Cell {
        return when(current.value) {
            Paths.PIPE -> if(from.row < current.row) {
                rows[current.row + 1][current.col]
            } else {
                rows[current.row - 1][current.col]
            }
            Paths.DASH -> if(from.col < current.col) {
                rows[current.row][current.col + 1]
            } else {
                rows[current.row][current.col - 1]
            }
            Paths.SEVEN -> if(from.row == current.row) {
                rows[current.row + 1][current.col]
            } else {
                rows[current.row][current.col - 1]
            }
            Paths.F -> if(from.row == current.row) {
                rows[current.row + 1][current.col]
            } else {
                rows[current.row][current.col + 1]
            }
            Paths.J -> if(from.row == current.row) {
                rows[current.row - 1][current.col]
            } else {
                rows[current.row][current.col - 1]
            }
            Paths.L -> if(from.row == current.row) {
                rows[current.row - 1][current.col]
            } else {
                rows[current.row][current.col + 1]
            }
            Paths.START -> findAdjacentToStart(current)
            else -> throw IllegalArgumentException("${current.value}")
        }
    }

    fun findAdjacentToStart(start: Cell): Cell {
        if (start.row > 0) {
            val top = rows[start.row - 1][start.row]
            if (top.value in listOf(Paths.F, Paths.SEVEN)) {
                return top
            }

            if (start.row > 1 && top.value == Paths.PIPE) {
                return top
            }
        }

        if (start.col > 0) {
            val left = rows[start.row][start.col - 1]
            if (left.value in listOf(Paths.F, Paths.L)) {
                return left
            }

            if (start.col > 1 && left.value == Paths.DASH) {
                return left
            }
        }

        if (start.row < rows.size - 1) {
            val bottom = rows[start.row + 1][start.col]
            if (bottom.value in listOf(Paths.L, Paths.J)) {
                return bottom
            }
            if (start.row < rows.size - 2 && bottom.value == Paths.PIPE) {
                return bottom
            }
        }

        if (start.col < rows.first().size - 1) {
            val right = rows[start.row][start.col + 1]
            if (right.value in listOf(Paths.SEVEN, Paths.J)) {
                return right
            }
            if (start.row < rows.size - 2 && right.value == Paths.DASH) {
                return right
            }
        }

        throw IllegalArgumentException("Start has no connected adjacents")
    }

    fun findAdjacents(cell: Cell): List<Cell> {
        val ret = mutableListOf<Cell>()
        if(cell.row > 0) {
            //if(cell.col > 0) {
            //    ret.add(rows[cell.row-1][cell.col-1])
            //}
            ret.add(rows[cell.row-1][cell.col])
            //if(cell.col < (rows.first().size -1)) {
            //    ret.add(rows[cell.row-1][cell.col+1])
            //}
        }

        if(cell.col > 0) {
            ret.add(rows[cell.row][cell.col-1])
        }

        if(cell.col < (rows.first().size -1)) {
            ret.add(rows[cell.row][cell.col+1])
        }

        if(cell.row < (rows.size -1)) {
            //if(cell.col > 0) {
            //    ret.add(rows[cell.row+1][cell.col-1])
            //}
            ret.add(rows[cell.row+1][cell.col])
            //if(cell.col < (rows.first().size -1)) {
            //    ret.add(rows[cell.row+1][cell.col+1])
            //}
        }

        return ret.toList()
    }
}

fun List<String>.toDay10Grid(): Day10Grid =
    Day10Grid(mapIndexed { rowIndex, line ->
        line.mapIndexed { index, cell ->
            Cell(rowIndex, index, Paths.entries.first { it.value == cell })
        }
    })


fun main() {

    fun part1(input: List<String>) : Long {
        val grid = input.toDay10Grid()
        grid.log()
        val start = grid.findStart()!!
        var count = 1
        var from = start
        var current = grid.findNext(start, start)
        while (current.value != Paths.START) {
            "Now processing node at (${current.row}, ${current.col}) with value ${current.value}".log()
            val nextFrom = current
            current = grid.findNext(current, from)
            from = nextFrom
            count++
        }
        return (count / 2).toLong()
    }

    fun part2(input: List<String>) : Int {
        val NORTH = -1 to 0
        val EAST = 0 to 1
        val SOUTH = 1 to 0
        val WEST = 0 to -1
        val points = run {
            lateinit var startPoint: Pair<Int, Int>
            val grid = input.flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c ->
                    (y to x) to when (c) {
                        'L' -> listOf(NORTH, EAST)
                        '|' -> listOf(NORTH, SOUTH)
                        'J' -> listOf(NORTH, WEST)
                        'F' -> listOf(EAST, SOUTH)
                        '-' -> listOf(EAST, WEST)
                        '7' -> listOf(SOUTH, WEST)
                        'S' -> listOf(NORTH, EAST, SOUTH, WEST).also { startPoint = y to x }
                        else -> emptyList()
                    }.map { (y2, x2) -> y2 + y to x2 + x }
                }
            }.toMap()
            val firstMove = grid.getValue(startPoint).first { from -> grid.getValue(from).any { it == startPoint } }
            generateSequence(startPoint to firstMove) { (from, to) ->
                when (to) {
                    startPoint -> null
                    else -> to to grid.getValue(to).minus(from).first()
                }
            }.map { it.first }.toList()
        }

        return points.plus(points.first())
            .zipWithNext { (y1, x1), (_, x2) -> (x2 - x1) * y1 }
            .sum().absoluteValue - (points.size / 2) + 1
    }

    val testInput = readInput("day10/Day10_test")
    val testInput2 = readInput("day10/Day10_test2")
    check(part1(testInput) == 4L)
    check(part1(testInput2) == 8L)

    val input = readInput("day10/Day10")
    part1(input).println()

    part2(input).println()
}