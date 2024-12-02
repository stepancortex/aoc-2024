package day2

import kotlin.io.path.Path

private fun getInput(): List<List<Long>> {
    return Path("resources/Day2.txt")
        .toFile()
        .readText()
        .split("\n")
        .map { it.split(" ") }
        .map { it.mapNotNull { n -> n.toLongOrNull() } }
}

fun p1() = getInput().filter { p1AnalyzeRow(it).first }.size.toLong()

fun p1AnalyzeRow(row: List<Long>): Pair<Boolean, Int> {
    val sign = if (row.first() > row.last()) -1L else 1L
    row.windowed(size = 2, step = 1).forEachIndexed { index, it ->
        val current = it[0]
        val next = it[1]
        val possibleOptions = setOf(current + sign * 1, current + sign * 2, current + sign * 3)
        if (next !in possibleOptions) {
            return false to index
        }
    }
    return true to 0
}

fun p2() = getInput().sumOf { p2AnalyzeRow(it) }

fun p2AnalyzeRow(row: List<Long>): Long {
    val result = p1AnalyzeRow(row)
    if (result.first) {
        return 1
    }
    for (i in 0..1) {
        val newRow = row.toMutableList()
        newRow.removeAt(result.second + i)
        val atRowResult = p1AnalyzeRow(newRow)
        if (atRowResult.first) {
            return 1
        }
    }
    return 0
}

fun main() {
    println(p1())
    println(p2())
}
