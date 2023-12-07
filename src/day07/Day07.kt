import kotlin.reflect.KClass

enum class Results(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

fun String.computeResult(): Results {
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

open class Hand(private val cards: String): Comparable<Hand> {

    protected open val result = cards.computeResult()
    protected open val cardsValues = "23456789TJQKA"

    override fun compareTo(other: Hand): Int {
        if (result == other.result) {
            for(i in cards.indices) {
                if (cards[i] != other.cards[i]) {
                    return cardsValues.indexOf(cards[i]).compareTo(cardsValues.indexOf(other.cards[i]))
                }
            }
            return 0
        }

        return result.rank.compareTo(other.result.rank)
    }
}

class HandPart2(private val cards: String): Hand(cards) {
    override val cardsValues = "J23456789TQKA"
    override val result = computeResult()

    private fun computeResult(): Results {
        return cardsValues.drop(1)
            .map { this.cards.replace('J', it).computeResult() }
            .maxBy { it.rank }
    }
}


fun doPart(input: List<String>, handClass: KClass<*>): Long {
    val hands = input.map { line ->
        val split = line.split(" ").map { it.trim() }

        val constructor = handClass.java.constructors.first()

        constructor.newInstance(split.first()) to split.last().toInt()
    }
    return hands.sortedBy { it.first as Hand }
        .mapIndexed { index, pair -> (index+1) * pair.second }
        .sum().toLong()
}

fun main() {
    
    fun part1(input: List<String>) = doPart(input, Hand::class)

    fun part2(input: List<String>) = doPart(input, HandPart2::class)


    val testInput = readInput("Day07/Day07_test")
    check(part1(testInput) == 6440L)

    val input = readInput("Day07/Day07")
    check(part1(input) == 250254244L)

    check(part2(testInput) == 5905L)
    check(part2(input) == 250087440L)
}