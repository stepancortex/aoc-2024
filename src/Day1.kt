package day1

import kotlin.io.path.Path
import kotlin.math.abs

fun getInput(): List<List<Long>> {
    return Path("resources/Day1.txt")
        .toFile()
        .readText()
        .split("\n")
        .map { it.split(" ") }
        .map { it.mapNotNull { n -> n.toLongOrNull() } }
}

fun p1(): Long {
    val input = getInput()
    val left = input.map { it[0] }.sorted()
    val right = input.map { it[1] }.sorted()
    return left.indices.sumOf { abs(left[it] - right[it]) }
}

fun p2(): Long {
    val input = getInput()
    val left = input.map { it[0] }
    val right = input.map { it[1] }.groupBy { it }.map { it.key to it.value.size.toLong() }.toMap()
    return left.sumOf { it * (right[it] ?: 0L) }
}

fun main() {
    println(p1())
    println(p2())
}
