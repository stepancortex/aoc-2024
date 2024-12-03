package day3

import kotlin.io.path.Path

fun getInput() = Path("resources/Day3.txt").toFile().readText().replace("\n", "")

fun calculate(input: String) =
    Regex("mul\\((\\d+,\\d+)\\)").findAll(input).sumOf {
        it.groups[1]!!.value.split(",").map { num -> num.toLong() }.reduce { acc, num -> acc * num }
    }

fun p1() = calculate(getInput())

fun p2() =
    Regex("do\\(\\)(.*?)(don't\\(\\)|$)").findAll("do()${getInput()}").sumOf {
        calculate(it.groups[1]!!.value)
    }

fun main() {
    println(p1())
    println(p2())
}
