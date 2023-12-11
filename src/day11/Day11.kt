import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SpaceCell(var row: Int, var col: Int, val value: Char)

class SpaceGrid(private var cells: MutableList<MutableList<SpaceCell>>) {

    val emptyRows = cells.filter { row -> row.all { it.value == '.'} }.map { it.first().row }
    val emptyCols = (0 until cells.first().size).filter { index ->
        cells.all { row -> row[index].value == '.' }
    }
    fun galaxies() = cells.flatMap { row -> row.filter { it.value == '#' } }
}

fun List<SpaceCell>.distances(emptyRows: List<Int>, emptyCols: List<Int>, expansionFactor: Long = 1): Long {
    val allDistances = this.flatMap { first ->
        this.map { second ->
            first to second
        }
    }.sumOf { pair ->
        val xDist = abs(pair.first.row - pair.second.row)
        val yDist = abs(pair.first.col - pair.second.col)
        val expansionFactorX = expansionFactor * (min(pair.first.row, pair.second.row)..max(pair.first.row, pair.second.row)).count { it in emptyRows }
        val expansionFactorY = expansionFactor * (min(pair.first.col, pair.second.col) .. max(pair.first.col, pair.second.col)).count { it in emptyCols }
        val distance = xDist + yDist + expansionFactorX + expansionFactorY
        "(${pair.first.row}, ${pair.first.col}) and (${pair.second.row}, ${pair.second.col}) have distance $distance".log()
        distance
    }

    return allDistances / 2L
}

fun main() {

    fun part1(input: List<String>): Long {
        val grid = SpaceGrid(input.mapIndexed { rowIndex, line ->
            line.mapIndexed { index, value -> SpaceCell(rowIndex, index, value) }.toMutableList()
        }.toMutableList())

        val galaxies = grid.galaxies()
        return galaxies.distances(grid.emptyRows, grid.emptyCols)
    }

    fun part2(input: List<String>, expansionFactor: Long): Long {
        val grid = SpaceGrid(input.mapIndexed { rowIndex, line ->
            line.mapIndexed { index, value -> SpaceCell(rowIndex, index, value) }.toMutableList()
        }.toMutableList())

        val galaxies = grid.galaxies()
        return galaxies.distances(grid.emptyRows, grid.emptyCols, expansionFactor)
    }

    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("day11/Day11")
    part1(input).println()
    check(part1(input) == 9418609L)

    check(part2(testInput, 9) == 1030L)
    check(part2(testInput, 99) == 8410L)
    part2(input, 999999).println()
}