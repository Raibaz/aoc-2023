fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val numbers = line.filter { it.isDigit() }
            ("" + numbers.first() + numbers.last()).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val digits = mapOf(
                "one" to "one1one",
                "two" to "two2two",
                "three" to "three3three",
                "four" to "four4four",
                "five" to "five5five",
                "six" to "six6six",
                "seven" to "seven7seven",
                "eight" to "eight8eight",
                "nine" to "nine9nine"
        )

        val processed = input.map { line ->
            var processedLine = line
            digits.forEach { digit ->
                processedLine = processedLine.replace(digit.key, digit.value)
            }
            processedLine
        }

        return part1(processed)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01/Day01")
    part1(input).println()
    val testInput2 = readInput("Day01/Day01_test2")
    check(part2(testInput2) == 281)
    part2(input).println()
}
