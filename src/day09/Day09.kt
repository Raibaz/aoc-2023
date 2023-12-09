fun List<Long>.bruteForceNext(): Long {
    val sequences = generateSequences()
    sequences.last().add(0)

    val reversed = sequences.drop(1).reversed()
    reversed.forEachIndexed { index, l ->
        val sequenceToEdit = sequences[sequences.size - 2 - index]
        sequenceToEdit.add(l.last() + sequenceToEdit.last())
    }
    return sequences.first().last()
}

fun List<Long>.bruteForcePrevious(): Long {
    val sequences = generateSequences()

    sequences.last().add(0, 0)

    val reversed = sequences.drop(1).reversed()
    reversed.forEachIndexed { index, l ->
        val sequenceToEdit = sequences[sequences.size - 2 - index]
        sequenceToEdit.add(0,  sequenceToEdit.first() - l.first())
    }
    return sequences.first().first()
}

fun List<Long>.generateSequences(): List<MutableList<Long>> {
    val sequences = mutableListOf(this.toMutableList())
    while(sequences.last().any { it != 0L }) {
        val nextSequence = sequences.last().drop(1).mapIndexed { index, l ->
            l - sequences.last()[index]
        }
        sequences.add(nextSequence.toMutableList())
    }
    return sequences
}

fun main() {

    fun part1(input: List<String>) : Long {
        return input.sumOf { line ->
            line.split(" ").map { it.trim().toLong() }.bruteForceNext()
        }
    }

    fun part2(input: List<String>) : Long {
        return input.sumOf { line ->
            line.split(" ").map { it.trim().toLong() }.bruteForcePrevious()
        }
    }

    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114L)

    val input = readInput("day09/Day09")
    part1(input).println()

    check(part2(testInput) == 2L)
    part2(input).println()
}