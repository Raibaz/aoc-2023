fun String.getArrangements(pattern: List<Int>): Long {
    val memo = IntArray(length) { i -> drop(i).takeWhile { c -> c != '.' }.length }
    val dp = mutableMapOf<Pair<Int, Int>, Long>()

    fun canTake(i: Int, l: Int) = memo[i] >= l && (i + l == length || this[i + l] != '#')
    fun helper(stringIndex: Int, patternIndex: Int): Long =
        when {
            patternIndex == pattern.size -> if (drop(stringIndex).none { c -> c == '#' }) {
                1L
            } else {
                0
            }
            stringIndex >= length -> 0L
            else -> {
                if (dp[stringIndex to patternIndex] == null) {
                    val take = if (canTake(stringIndex, pattern[patternIndex])) {
                        helper(stringIndex + pattern[patternIndex] + 1, patternIndex + 1)
                    } else {
                        0L
                    }
                    val dontTake = if (this[stringIndex] != '#') {
                        helper(stringIndex + 1, patternIndex)
                    } else {
                        0L
                    }
                    dp[stringIndex to patternIndex] = take + dontTake
                }
                dp[stringIndex to patternIndex]!!
            }
        }
    return helper(0, 0)
}
fun main() {

    fun part1(input: List<String>) = input
        .map { line -> line.split(" ").let { (l, r) -> l to r } }
        .sumOf { (s, counts)  ->
            val pattern = counts.split(",").map(String::toInt)
            val count = s.getArrangements(pattern)
            "$s has $count arrangements of pattern $counts".log()
            count
        }

    fun part2(input: List<String>) = input
        .map { line -> line.split(" ").let { (l, r) -> l to r } }
        .sumOf { (s, counts) ->
            "$s?$s?$s?$s?$s".getArrangements("$counts,$counts,$counts,$counts,$counts".split(",").map(String::toInt))
        }

    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21L)

    val input = readInput("day12/Day12")
    part1(input).println()
    check(part1(input) == 7541L)

    check(part2(testInput) == 525152L)
    part2(input).println()
}