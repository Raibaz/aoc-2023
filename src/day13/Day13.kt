fun List<String>.isMirrorRow(rowIndex: Int): Boolean {
    if (rowIndex == size - 1) {
        return false
    }
    var index = 0
    while ((rowIndex - index) >= 0 && (rowIndex + index + 1) < size) {
        val above = this[rowIndex - index]
        val below = this[rowIndex + 1 + index]
        if (above != below) {
            return false
        }
        index++
    }
    return true
}

fun List<String>.isMirrorCol(colIndex: Int): Boolean {
    if (colIndex >= first().length - 1) {
        return false
    }
    var index = 0
    while ((colIndex - index) >= 0 && (colIndex + index + 1) < first().length) {
        val left = this.extractColumn(colIndex - index)
        val right = this.extractColumn(colIndex + 1 + index)
        if (left != right) {
            return false
        }
        index++
    }
    return true
}

fun List<String>.extractColumn(colIndex: Int) = joinToString("") { it[colIndex].toString() }

fun main() {
    fun part1(input: List<String>) : Long {
        var inputIndex = 0
        var result = 0L
        while (inputIndex < input.size) {
            val pattern = input.drop(inputIndex).takeWhile { it.isNotEmpty() }
            ("  " + (1..pattern.first().length).joinToString("")).log()
            pattern.forEachIndexed { index, line -> "${index+1} $line".log() }
            val mirrorRows = pattern.indices.filter { pattern.isMirrorRow(it) }.sumOf { it + 1 }
            val mirrorCols = pattern.first().indices.filter { pattern.isMirrorCol(it) }.sumOf { it + 1 }
            "MirrorRows = $mirrorRows, mirrorCols = $mirrorCols".log()
            result += ((mirrorRows * 100) + mirrorCols).toLong()
            inputIndex += pattern.size + 1
        }
        return result
    }

    fun part2(input: List<String>) : Long {
        return 1
    }

    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 405L)
    val testInput2 = readInput("day13/Day13_test2")
    check(part1(testInput2) == 9L)

    val input = readInput("day13/Day13")
    part1(input).println()
    check(part1(input) == 40006L)

    check(part2(testInput) == 525152L)
    part2(input).println()
}