package day7

import kotlin.io.path.Path
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

enum class Operation {
    ADD,
    MULTIPLY,
    CONCAT;

    fun execute(a: Long, b: Long): Long {
        return when (this) {
            ADD -> a + b
            MULTIPLY -> a * b
            CONCAT -> {
                val digits = floor(log10(b.toDouble())).toInt() + 1
                return a * 10.0.pow(digits).toLong() + b
            }
        }
    }
}

fun getInput() =
    Path("resources/Day7.txt").toFile().readText().split("\n").map {
        val parts = it.split(":")
        val nums = parts[1].split(" ").mapNotNull { num -> num.toLongOrNull() }
        return@map Pair<Long, ArrayDeque<Long>>(parts[0].toLong(), ArrayDeque(nums))
    }

fun p1(input: List<Pair<Long, ArrayDeque<Long>>>): Long {
    return helper(input, listOf(Operation.MULTIPLY, Operation.ADD))
}

fun p2(input: List<Pair<Long, ArrayDeque<Long>>>): Long {
    return helper(input, listOf(Operation.CONCAT, Operation.MULTIPLY, Operation.ADD))
}

fun helper(input: List<Pair<Long, ArrayDeque<Long>>>, operations: List<Operation>): Long {
    return input.filter { evaluate(it.first, it.second, operations) }.sumOf { it.first }
}

fun evaluate(number: Long, equation: ArrayDeque<Long>, operations: List<Operation>): Boolean {
    if (equation.size == 1) {
        return number == equation[0]
    }
    operations.forEach { operation ->
        val a = equation.removeFirst()
        val b = equation.removeFirst()
        val result = operation.execute(a, b)
        if (result <= number) {
            equation.addFirst(result)
            if (evaluate(number, equation, operations)) {
                return true
            }
            equation.removeFirst()
        }
        equation.addFirst(b)
        equation.addFirst(a)
    }

    return false
}

fun main() {
    val input = getInput()
    println(p1(input))
    println(p2(input))
}
