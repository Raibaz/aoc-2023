fun List<String>.parse(): List<Pair<Long, Long>> {
    val times = first().replace("Time:", "").trim().split(" ").filter { it.isNotEmpty() }.map { it.trim().toLong() }
    val distances = last().replace("Distance:", "").trim().split(" ").filter { it.isNotEmpty() }.map { it.trim().toLong() }

    return times.zip(distances)
}

fun List<String>.parsePart2(): Pair<Long, Long> {
    val time = first().replace("Time:", "").replace(" ", "").trim().toLong()
    val distance = last().replace("Distance:", "").replace(" ", "").trim().toLong()

    return time to distance
}

fun Pair<Long, Long>.countWins(): Long {
    var count = 0L
    for (i in 0..first) {
        val speed = i
        val distance = (first - i) * speed
        if (distance > second) {
            count++
        }
    }

    return count
}

fun main() {

    fun part1(input: List<String>): Long {
        return input.parse()
            .map { it.countWins() }
            .fold(1) { current, next -> current * next }
    }

    fun part2(input: List<String>): Long {
        return input.parsePart2().countWins()
    }

    val testInput = readInput("Day06/Day06_test")
    check(part1(testInput) == 288L)

    val input = readInput("Day06/Day06")
    part1(input).println()

    check(part2(testInput) == 71503L)
    part2(input).println()

}