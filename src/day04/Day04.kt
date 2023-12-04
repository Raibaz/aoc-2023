import kotlin.math.pow

fun String.countWinningNumbers(): Int {
    val winningNumbers = substring(indexOf(":")+1, indexOf("|"))
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }

    val numbersYouHave = substring(indexOf("|")+1)
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }

    return winningNumbers.intersect(numbersYouHave.toSet()).size
}
fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val winningNumbersYouHave = line.countWinningNumbers()
            val base = 2
            base.toDouble().pow(winningNumbersYouHave - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cards = mutableMapOf<Int, Int>()
        input.forEach { line ->
            val gameIndex = line.substring(0, line.indexOf(":")).replace("Card ", "").trim().toInt()
            cards[gameIndex] = cards.getOrDefault(gameIndex, 0) + 1
            val winningNumbers = line.countWinningNumbers()
            for(i in (gameIndex+1).rangeTo(gameIndex + winningNumbers)) {
                val current = cards.getOrDefault(i, 0)
                cards[i] = current + cards[gameIndex]!!
            }
        }
        return cards.values.sum()
    }

    val testInput = readInput("Day04/Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04/Day04")
    part1(input).println()

    check(part2(testInput) == 30)
    part2(input).println()

}