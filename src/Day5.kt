package day5

import kotlin.io.path.Path

fun getInput() = Path("resources/Day5.txt").toFile().readText().split("\n\n")

fun p1(dependency: Map<Long, Set<Long>>, updates: List<List<Long>>): Long {
    return updates.filter { isValid(dependency, it) }.sumOf { it[it.size / 2] }
}

fun createUpdates(input: String): List<List<Long>> {
    return input.split("\n").map { it.split(",").map { num -> num.toLong() } }
}

fun createDependencyMapping(input: String): Map<Long, Set<Long>> {
    val parsed = input.split("\n").map { it.split("|").map { num -> num.toLong() } }
    val output = parsed.flatten().associateWith { mutableSetOf<Long>() }
    parsed.forEach { output[it[1]]!!.add(it[0]) }
    return output
}

fun p2(dependency: Map<Long, Set<Long>>, updates: List<List<Long>>): Long {
    return updates
        .filter { !isValid(dependency, it) }
        .map { fixUpdate(dependency, it) }
        .sumOf { it[it.size / 2] }
}

fun isValid(dependency: Map<Long, Set<Long>>, update: List<Long>): Boolean {
    val updateSet = update.toSet()
    val processed = mutableSetOf<Long>()
    update.forEach {
        val required = dependency[it]!!
        val remaining = updateSet.minus(processed).minus(it)
        val notProcessed = required.intersect(remaining)
        if (notProcessed.isNotEmpty()) {
            return false
        }
        processed.add(it)
    }
    return true
}

fun fixUpdate(dependency: Map<Long, Set<Long>>, update: List<Long>): List<Long> {
    val stack = update.toMutableList()
    val final = mutableListOf<Long>()
    while (stack.isNotEmpty()) {
        val num = stack.removeAt(0)
        val required = dependency[num]!!
        val shouldComeBeforeNum = required.minus(final.toSet()).intersect(stack.toSet())
        if (shouldComeBeforeNum.isEmpty()) {
            final.add(num)
        } else {
            stack.removeAll(shouldComeBeforeNum)
            stack.add(0, num)
            stack.addAll(0, shouldComeBeforeNum)
        }
    }
    return final
}

fun main() {
    val values = getInput()
    val dependency = createDependencyMapping(values[0])
    val updates = createUpdates(values[1])
    println(p1(dependency, updates))
    println(p2(dependency, updates))
}
