package day4

import kotlin.io.path.Path

private val MAS = listOf('M', 'A', 'S')
private val MMASS = Regex("M.M.A.S.S")
private val SSAMM = Regex("S.S.A.M.M")
private val MSAMS = Regex("M.S.A.M.S")
private val SMASM = Regex("S.M.A.S.M")

fun getInput() = Path("resources/Day4.txt").toFile().readText().split("\n").map { it.toList() }

enum class Direction {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    fun nextCoordinate(): Pair<Int, Int> {
        return when (this) {
            NORTH -> Pair(-1, 0)
            NORTH_EAST -> Pair(-1, 1)
            EAST -> Pair(0, 1)
            SOUTH_EAST -> Pair(1, 1)
            SOUTH -> Pair(1, 0)
            SOUTH_WEST -> Pair(1, -1)
            WEST -> Pair(0, -1)
            NORTH_WEST -> Pair(-1, -1)
        }
    }
}

fun Pair<Int, Int>.add(pair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + pair.first, this.second + pair.second)
}

fun p1(): Int {
    val matrix = getInput()
    val options =
        p1FindAllXCoordinates(matrix)
            .map { pair ->
                Direction.entries.map { direction ->
                    Pair(pair.add(direction.nextCoordinate()), direction)
                }
            }
            .flatten()
    return options.sumOf { p1Helper(it.first.first, it.first.second, matrix, 0, it.second) }
}

fun p1FindAllXCoordinates(matrix: List<List<Char>>): List<Pair<Int, Int>> {
    return matrix
        .mapIndexed { i, row ->
            row.mapIndexedNotNull inner@{ j, item ->
                if (item == 'X') {
                    return@inner Pair(i, j)
                }
                return@inner null
            }
        }
        .flatten()
}

fun p1Helper(i: Int, j: Int, matrix: List<List<Char>>, index: Int, direction: Direction): Int {
    val letter = MAS[index]
    try {
        if (matrix[i][j] != letter) {
            return 0
        }
    } catch (error: IndexOutOfBoundsException) {
        return 0
    }
    if (index + 1 == MAS.size) {
        return 1
    }
    val next = Pair(i, j).add(direction.nextCoordinate())
    return p1Helper(next.first, next.second, matrix, index + 1, direction)
}

fun p2(): Long {
    val options = p2GetAllPossibleOptions(getInput())
    val regexes = listOf(MMASS, SSAMM, MSAMS, SMASM)
    return options.sumOf { item ->
        regexes.sumOf inner@{
            if (it.matches(item)) {
                return@inner 1L
            }
            return@inner 0L
        }
    }
}

fun p2GetAllPossibleOptions(matrix: List<List<Char>>): List<String> {
    return matrix
        .windowed(3)
        .map {
            (0..<it[0].size).windowed(3).withIndex().map { i ->
                it.joinToString(separator = "") { row ->
                    (0..2).map { b -> row[i.value[b]] }.joinToString(separator = "")
                }
            }
        }
        .flatten()
}

fun main() {
    println(p1())
    println(p2())
}
