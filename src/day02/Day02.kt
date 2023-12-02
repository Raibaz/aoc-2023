fun String.isValidExtraction(): Boolean {
    val thresholds = mapOf(
            "blue" to 14,
            "red" to 12,
            "green" to 13
    )
    return this.split(",").all { color ->
        val split = color.trim().split(" ")
        thresholds[split[1]]!! >= split[0].toInt()
    }
}

fun String.computePower(): Int {
    val values = mutableMapOf(
            "green" to 0,
            "blue" to 0,
            "red" to 0
    )

    return this.split(":")[1].trim().split(";").fold(values) { current, extraction ->
        extraction.split(",").fold(current) { currentForColor, entry ->
            val color = entry.trim().split(" ")[1].trim()
            val value = entry.trim().split(" ")[0].trim().toInt()
            currentForColor[color] = kotlin.math.max(values[color] ?: 0, value)
            currentForColor
        }
        current
    }.values.fold(1) { total, next -> total * next}
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val splitLine = line.split(":")
            val game = splitLine[1]
            val extractions = game.split(";")
            if(extractions.all { it.isValidExtraction() }) {
                splitLine[0].replace("Game ", "").toInt()
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            line.computePower()
        }
    }

    val testInput = readInput("Day02/Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02/Day02")
    part1(input).println()
    check(part2(testInput) == 2286)
    part2(input).println()
}