fun String.hash(): Int {
    var ret = 0
    forEach { c ->
        ret += c.code
        ret *= 17
        ret %= 256
    }

    return ret
}

fun main() {
    fun part1(input: List<String>) = input.first().split(",").sumOf { it.hash() }.toLong()

    fun part2(input: List<String>) : Long {
        val map = mutableMapOf<Int, MutableList<Pair<String, Int>>>()
        input.first().split(",").forEach { instruction ->
            val label = instruction.takeWhile { it != '=' && it != '-' }
            val hash = label.hash()
            if (instruction.contains("=")) {
                val value = label to instruction.dropWhile { it != '=' }.drop(1).toInt()
                if (!map.containsKey(hash)) {
                    map[hash] = mutableListOf(value)
                    return@forEach
                }

                val index = map[hash]!!.indexOfFirst { it.first == value.first }
                if (index == -1) {
                    map[hash]!!.add(value)
                } else {
                    map[hash]!!.removeAt(index)
                    map[hash]!!.add(index, value)
                }
            } else {
                map[hash]?.removeIf { it.first == label }
            }
        }

        return map.keys.sumOf { key ->
            map[key]!!.mapIndexed { index, value ->
                (key + 1) * (index + 1) * value.second
            }.sum()
        }.toLong()

    }

    check("HASH".hash() == 52)

    val testInput = readInput("day15/Day15_test")
    check(part1(testInput) == 1320L)

    val input = readInput("day15/Day15")
    part1(input).println()
    check(part1(input) == 519603L)

    check(part2(testInput) == 145L)
    part2(input).println()
    check(part2(input) == 244342L)
}