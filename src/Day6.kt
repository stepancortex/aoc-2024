package day6

import kotlin.io.path.Path

private const val LEFT = "<"
private const val RIGHT = ">"
private const val UP = "^"
private const val DOWN = "v"
private const val OBSTRUCTION = "#"
private const val VISITED = "X"
private val GUARD_DIRECTIONS = setOf(LEFT, RIGHT, UP, DOWN)

fun getInput() =
    Path("resources/Day6.txt").toFile().readText().split("\n").map {
        it.toList().map { char -> char.toString() }
    }

fun String.nextLocation(): Pair<Int, Int> {
    if (this == LEFT) {
        return Pair(0, -1)
    }
    if (this == RIGHT) {
        return Pair(0, 1)
    }
    if (this == UP) {
        return Pair(-1, 0)
    }
    return Pair(1, 0)
}

fun String.nextDirection(): String {
    if (this == LEFT) {
        return UP
    }
    if (this == RIGHT) {
        return DOWN
    }
    if (this == UP) {
        return RIGHT
    }
    return LEFT
}

fun Pair<Int, Int>.add(pair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + pair.first, this.second + pair.second)
}

fun p1(): Long {
    val map = getInput()
    val visited = visit(map, findGuardLocation(map))
    return visited.sumOf { row ->
        row.sumOf row@{
            return@row if (it == VISITED) {
                1L
            } else 0
        }
    }
}

fun visit(map: List<List<String>>, guardLocation: Pair<Int, Int>): List<List<String>> {
    val mapCopy = map.map { it.toMutableList() }.toMutableList()
    val visited = map.map { it.toMutableList() }.toMutableList()
    var currentLocation = guardLocation
    while (true) {
        try {
            currentLocation =
                visitLocation(currentLocation.first, currentLocation.second, mapCopy, visited)
        } catch (error: IndexOutOfBoundsException) {
            break
        }
    }
    return visited
}

fun visitP2(
    map: List<List<String>>,
    guardLocation: Pair<Int, Int>,
    additionalObstruction: Pair<Int, Int>,
): Long {
    val mapCopy = map.map { it.toMutableList() }.toMutableList()
    mapCopy[additionalObstruction.first][additionalObstruction.second] = OBSTRUCTION
    val visited = map.map { it.toMutableList() }.toMutableList()
    var currentLocation = guardLocation
    val seen = mutableSetOf<Triple<Int, Int, String>>()
    while (true) {
        currentLocation =
            try {

                visitLocation(currentLocation.first, currentLocation.second, mapCopy, visited)
            } catch (error: IndexOutOfBoundsException) {
                return 0
            }
        val current =
            Triple(
                currentLocation.first,
                currentLocation.second,
                mapCopy[currentLocation.first][currentLocation.second],
            )
        if (current in seen) {
            return 1
        }
        seen.add(current)
    }
}

fun visitLocation(
    i: Int,
    j: Int,
    map: MutableList<MutableList<String>>,
    visited: MutableList<MutableList<String>>,
): Pair<Int, Int> {
    visited[i][j] = VISITED
    val guardDirection = map[i][j]
    val nextLocation = guardDirection.nextLocation().add(Pair(i, j))
    if (map[nextLocation.first][nextLocation.second] == OBSTRUCTION) {
        map[i][j] = guardDirection.nextDirection()
        return Pair(i, j)
    }
    map[i][j] = "."
    map[nextLocation.first][nextLocation.second] = guardDirection
    return nextLocation
}

fun findGuardLocation(map: List<List<String>>): Pair<Int, Int> {
    map.forEachIndexed { i, row ->
        row.forEachIndexed { j, item ->
            if (item in GUARD_DIRECTIONS) {
                return Pair(i, j)
            }
        }
    }
    throw IllegalStateException("No guard location")
}

fun p2(): Long {
    val map = getInput()
    val guardLocation = findGuardLocation(map)
    val visited = visit(map, guardLocation)
    val visitedCoordinates =
        visited
            .mapIndexed { i, row ->
                row.mapIndexedNotNull inner@{ j, item ->
                    return@inner if (item == VISITED) {
                        Pair<Int, Int>(i, j)
                    } else {
                        null
                    }
                }
            }
            .flatten()

    val result = visitedCoordinates.sumOf { visitP2(map, guardLocation, it) }

    return result
}

fun main() {
    println(p1())
    println(p2())
}
