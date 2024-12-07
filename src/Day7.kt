package day7

import kotlin.io.path.Path

enum class Operation {
    ADD,
    MULTIPLY,
    CONCAT;

    fun execute(a: Long, b: Long): Long {
        return when (this) {
            ADD -> a + b
            MULTIPLY -> a * b
            CONCAT -> "${a}${b}".toLong()
        }
    }
}

fun getInput() =
    Path("resources/Day7.txt").toFile().readText().split("\n").map {
        val parts = it.split(":")
        val nums = parts[1].split(" ").mapNotNull { num -> num.toLongOrNull() }
        return@map Pair<Long, List<Long>>(parts[0].toLong(), nums)
    }

fun p1(input: List<Pair<Long, List<Long>>>): Long {
    return helper(input, listOf(Operation.MULTIPLY, Operation.ADD))
}

fun p2(input: List<Pair<Long, List<Long>>>): Long {
    return helper(input, listOf(Operation.CONCAT, Operation.MULTIPLY, Operation.ADD))
}

fun helper(input: List<Pair<Long, List<Long>>>, operations: List<Operation>): Long {
    return input.filter { evaluate(it.first, it.second, operations) }.sumOf { it.first }
}

fun evaluate(number: Long, equation: List<Long>, operations: List<Operation>): Boolean {
    if (equation.size == 1) {
        return number == equation[0]
    }
    val operationsCopy = operations.toMutableList()
    while (operationsCopy.isNotEmpty()) {
        val equationCopy = equation.toMutableList()
        val operation = operationsCopy.removeAt(0)
        val a = equationCopy.removeAt(0)
        val b = equationCopy.removeAt(0)
        val result = operation.execute(a, b)
        if (result <= number) {
            equationCopy.add(0, result)
            if (evaluate(number, equationCopy, operations)) {
                return true
            }
        }
    }

    return false
}

fun main() {
    val input = getInput()
    println(p1(input))
    println(p2(input))
}
