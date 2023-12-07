val CARDS_VALUES = "23456789TJQKA"
val CARDS_VALUES_PART_2 = "J23456789TQKA"
enum class Results(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

class Hand(private val cards: String, val isPart2: Boolean = false): Comparable<Hand> {

    val result = cards.computeResult()
    val resultPart2 = computeResultPart2()
    private fun String.computeResult(): Results {
        val map = mutableMapOf<Char, Int>()
        this.forEach {
            map[it] = map.getOrDefault(it, 0) + 1
        }
        val max = map.values.max()
        return when(max) {
            5 -> Results.FIVE_OF_A_KIND
            4 -> Results.FOUR_OF_A_KIND
            3 -> {
                if (map.values.sortedDescending()[1] == 2) {
                    Results.FULL_HOUSE
                } else {
                    Results.THREE_OF_A_KIND
                }
            }
            2 -> {
                if (map.values.sortedDescending()[1] == 2) {
                    Results.TWO_PAIR
                } else {
                    Results.ONE_PAIR
                }
            }
            else -> Results.HIGH_CARD
        }
    }

    private fun computeResultPart2(): Results {
        return CARDS_VALUES_PART_2.drop(1)
            .map { this.cards.replace('J', it).computeResult() }
            .maxBy { it.rank }
    }

    override fun compareTo(other: Hand): Int {
        if (isPart2) {
            if (resultPart2 == other.resultPart2) {
                for(i in cards.indices) {
                    if (cards[i] != other.cards[i]) {
                        return CARDS_VALUES_PART_2.indexOf(cards[i]).compareTo(CARDS_VALUES_PART_2.indexOf(other.cards[i]))
                    }
                }
                return 0
            }

            return resultPart2.rank.compareTo(other.resultPart2.rank)
        } else {
            if (result == other.result) {
                for(i in cards.indices) {
                    if (cards[i] != other.cards[i]) {
                        return CARDS_VALUES.indexOf(cards[i]).compareTo(CARDS_VALUES.indexOf(other.cards[i]))
                    }
                }
                return 0
            }

            return result.rank.compareTo(other.result.rank)
        }

    }
}

fun main() {

    fun part1(input: List<String>): Long {
        val hands = input.map {
            val split = it.split(" ").map { it.trim() }
            Hand(split.first()) to split.last().toInt()
        }
        return hands.sortedBy { it.first }
            .mapIndexed { index, pair -> (index+1) * pair.second }
            .sum().toLong()
    }

    fun part2(input: List<String>): Long {
        val hands = input.map {
            val split = it.split(" ").map { it.trim() }
            Hand(split.first(), true) to split.last().toInt()
        }
        return hands.sortedBy { it.first }
            .mapIndexed { index, pair -> (index+1) * pair.second }
            .sum().toLong()
    }

    val testInput = readInput("Day07/Day07_test")
    //check(part1(testInput) == 6440L)

    val input = readInput("Day07/Day07")
    //part1(input).println()

    check(part2(testInput) == 5905L)
    part2(input).println()

}