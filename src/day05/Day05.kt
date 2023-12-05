import java.util.concurrent.atomic.AtomicLong
import kotlin.streams.asStream

class Filter(private val source: Long, private val destination: Long, private val size: Long) {
    fun filter(input: Long): Long? {
        return if(input >= source && input < (source + size)) {
            destination + (input - source)
        } else {
            null
        }
    }
}

class SeedRange(private val source: Long, private val size: Long) {
    fun minFiltered(allFilters: List<List<Filter>>): Long {
        "Filtering range from $source with size $size...".log()
        return source.until(source+size).minOf {
            allFilters.fold(it) { current, next ->
                current.filter(next)
            }
        }.also { it.log() }
    }
}

fun String.parseSeeds(): List<Long> = replace("seeds: ", "").trim().split(" ").map { it.toLong() }

fun String.parseSeedsPart2(): List<SeedRange> = replace("seeds: ", "").trim().split(" ").windowed(2, 2).map {
    SeedRange(it.first().toLong(), it.last().toLong())
}

fun List<String>.parseFilters(): List<Filter> = map { line ->
    val split = line.split(" ")
    Filter(split[1].toLong(), split[0].toLong(), split[2].toLong())
}

fun List<String>.extractMapInput(header: String): List<String> = dropWhile { it != header }.drop(1).takeWhile { it.isNotEmpty() }

fun Long.filter(filters: List<Filter>): Long = filters.firstNotNullOfOrNull { it.filter(this) } ?: this

fun List<String>.parseAllFilters(): List<List<Filter>> = listOf(
    extractMapInput("seed-to-soil map:").parseFilters(),
    extractMapInput("soil-to-fertilizer map:").parseFilters(),
    extractMapInput("fertilizer-to-water map:").parseFilters(),
    extractMapInput("water-to-light map:").parseFilters(),
    extractMapInput("light-to-temperature map:").parseFilters(),
    extractMapInput("temperature-to-humidity map:").parseFilters(),
    extractMapInput("humidity-to-location map:").parseFilters()
)
fun main() {

    fun part1(input: List<String>): Long {
        val seeds = input.first().parseSeeds()
        val allFilters = input.parseAllFilters()

        return seeds.minOf {
            allFilters.fold(it) { current, next ->
                current.filter(next)
            }
        }
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input.first().parseSeedsPart2()
        val allFilters = input.parseAllFilters()

        return seedRanges.minOf { it.minFiltered(allFilters) }
    }

    val testInput = readInput("Day05/Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05/Day05")
    part1(input).println()

    check(part2(testInput) == 46L)
    part2(input).println()

}