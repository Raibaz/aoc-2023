fun Any?.log() {
    val debug = false
    if (debug) {
        this.println()
    }
}

typealias Grid = List<List<Char>>
typealias Point = Pair<Int, Int>
fun Grid.get(coords: Point?): Char? {
    return if (coords == null) {
        null
    } else {
        this[coords.first][coords.second]
    }
}

fun List<String>.toGrid(): Grid = this.map { line -> line.toCharArray().toList() }

fun Grid.getAdjacents(coords: Point): List<Char> {
    val ret = mutableListOf<Char>()
    if(coords.first > 0) {
        if(coords.second > 0) {
            ret.add(this[coords.first-1][coords.second-1])
        }
        ret.add(this[coords.first-1][coords.second])
        if(coords.second < (this.first().size -1)) {
            ret.add(this[coords.first-1][coords.second+1])
        }
    }

    if(coords.second > 0) {
        ret.add(this[coords.first][coords.second-1])
    }

    if(coords.second < (this.first().size -1)) {
        ret.add(this[coords.first][coords.second+1])
    }

    if(coords.first < (this.size -1)) {
        if(coords.second > 0) {
            ret.add(this[coords.first+1][coords.second-1])
        }
        ret.add(this[coords.first+1][coords.second])
        if(coords.second < (this.first().size -1)) {
            ret.add(this[coords.first+1][coords.second+1])
        }
    }

    return ret.toList()
}

fun Grid.isValidDigit(coords: Point): Boolean = getAdjacents(coords).any { !it.isDigit() && it != '.'}

fun String.extractValidNumbersSum(grid: Grid, lineIndex: Int): Int {
    var ret = 0

    var currentNumber = ""
    var index = 0
    var isValid = false
    while (index < this.length) {
        if(!this[index].isDigit()) {
            if(currentNumber.isNotEmpty()) {
                if(isValid) {
                    "$currentNumber is valid".log()
                    ret += currentNumber.toInt()
                } else {
                    "$currentNumber is not valid".log()
                }
            }
            currentNumber = ""
            isValid = false
        } else {
            currentNumber += this[index]
            isValid = isValid || grid.isValidDigit(lineIndex to index)
        }
        index++
    }

    if(currentNumber.isNotEmpty()) {
        if(isValid) {
            "$currentNumber is valid".log()
            ret += currentNumber.toInt()
        } else {
            "$currentNumber is not valid".log()
        }
    }

    return ret
}

fun Grid.extractAllStarPositions(): List<Point> = flatMapIndexed { rowIndex, row ->
    row.mapIndexedNotNull { colIndex, value ->
        if (value == '*') {
            rowIndex to colIndex
        } else {
            null
        }
    }
}

fun Grid.extractAllAdjacentNumbers(point: Point): List<Int> {
    "Extracting adjacents for point $point".log()
    val sameLineLeft = extractAdjacentSameLineLeft(point).also { it?.log() }
    val sameLineRight = extractAdjacentSameLineRight(point).also { it?.log() }
    val prevLine = extractAdjacentPrevLine(point).also { it.log() }
    val nextLine = extractAdjacentNextLine(point).also { it.log() }
    "Done.".log()
    return listOfNotNull(sameLineLeft?.toInt(), sameLineRight?.toInt()) + prevLine + nextLine
}

fun Grid.extractAdjacentSameLineLeft(point: Point): String? {
    val line = this[point.first]
    var index = point.second - 1
    var ret = ""
    while(index >= 0) {
        if(line[index].isDigit()) {
            ret += line[index]
        } else {
            break
        }
        index--
    }
    return if(ret.isNotEmpty()) {
        ret.reversed()
    } else {
        null
    }
}

fun Grid.extractAdjacentSameLineRight(point: Point): String? {
    val line = this[point.first]
    var index = point.second + 1
    var ret = ""
    while(index > 0 && index < line.size) {
        if(line[index].isDigit()) {
            ret += line[index]
        } else {
            break
        }
        index++
    }
    return ret.ifEmpty {
        null
    }
}

fun Grid.extractAdjacentPrevLine(point: Point): List<Int> {
    if(point.first <= 0) {
        return listOf()
    }

    val upper = point.first - 1 to point.second
    return extractAdjacentToPoint(upper)
}

fun Grid.extractAdjacentNextLine(point: Point): List<Int> {
    if(point.first >= this.size - 1) {
        return listOf()
    }

    val lower = point.first + 1 to point.second
    return extractAdjacentToPoint(lower)
}

fun Grid.extractAdjacentToPoint(point: Point): List<Int> {
    return if(!get(point)!!.isDigit()) {
        listOfNotNull(
                extractAdjacentSameLineLeft(point)?.toInt(),
                extractAdjacentSameLineRight(point)?.toInt(),
        )
    } else {
        val left = (extractAdjacentSameLineLeft(point) ?: "").toString()
        val right = (extractAdjacentSameLineRight(point) ?: "").toString()
        val ret = "$left${get(point)!!}$right".toInt()
        listOf(ret)
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        return input.mapIndexed { lineIndex, line ->
            line.extractValidNumbersSum(grid, lineIndex)
        }.also{it.log()}.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        val allStars = grid.extractAllStarPositions()
        return allStars
            .associateWith { grid.extractAllAdjacentNumbers(it) }
            .also { it.log() }
            .filter { it.value.size == 2 }
            .also { it.log() }
            .map { it.value.fold(1) { total, next -> total * next} }
            .sum()
    }

    val testInput = readInput("Day03/Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03/Day03")
    part1(input).println()
    check(part1(input) == 551094)
    check(part2(testInput) == 467835)
    part2(input).println()
    check(part2(input) == 80179647)
}

