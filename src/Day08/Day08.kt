fun List<String>.findNode(label: String) = indexOfFirst { it.label() == label }
fun String.label() = substring(0, 3)
fun String.left() = substring(7, 10)
fun String.right() = substring(12, 15)

fun lcm(a: Long, b: Long) = a*b/gcd(a, b)

fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    if (a < 0 || b < 0 || a + b <= 0) {
        throw IllegalArgumentException("GCD Error: a=$a, b=$b")
    }

    while (a > 0 && b > 0) {
        if (a >= b) {
            a %= b
        } else {
            b %= a
        }
    }

    return maxOf(a, b)
}

fun List<String>.stepsFromStart(start: String, directions: String): Long {
    var steps = 0L
    var currentNode = this[findNode(start)]
    while(!currentNode.label().endsWith("Z")) {
        val nextNodeIndex = if (directions[(steps % directions.length).toInt()] == 'L') {
            findNode(currentNode.left())
        } else {
            findNode(currentNode.right())
        }
        currentNode = this[nextNodeIndex]
        println(currentNode)
        steps++
    }

    return steps
}

fun main() {

    fun part1(input: List<String>) : Long {
        val directions = input.first()
        val nodes = input.drop(2)
        return nodes.stepsFromStart("AAA", directions)
    }

    fun part2(input: List<String>) : Long {
        val directions = input.first()
        val nodes = input.drop(2)

        return nodes.filter { it.label().endsWith("A") }.map { nodes.stepsFromStart(it.label(), directions) }.reduce(::lcm)
    }

    val testInput = readInput("Day08/Day08_test")
    check(part1(testInput) == 2L)

    val testInput2 = readInput("Day08/Day08_test2")
    check(part1(testInput2) == 6L)

    val input = readInput("Day08/Day08")
    part1(input).println()

    val testInput3 = readInput("Day08/Day08_test3")

    check(part2(testInput3) == 6L)
    part2(input).println()
}