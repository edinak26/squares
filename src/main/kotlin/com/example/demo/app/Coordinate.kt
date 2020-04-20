package com.example.demo.app

import kotlin.random.Random

infix fun Int.cord(columnNumber: Int) = Coordinate(this, columnNumber)
fun generateRandomCoordinate() = Coordinate(Random.nextInt(LINES_NUMBER),Random.nextInt(COLUMN_NUMBER))

operator fun <T> Array<Array<T>>.get(coordinate: Coordinate) = this[coordinate.line][coordinate.column]
operator fun <T> Array<Array<T>>.set(coordinate: Coordinate, value: T) {
    this[coordinate.line][coordinate.column] = value
}

class Coordinate(lineNumber: Int, columnNumber: Int) {
    val line: Int = roundCoordinate(lineNumber, LINES_NUMBER - 1)
    val column: Int = roundCoordinate(columnNumber, COLUMN_NUMBER - 1)

    fun coordinateIn(direction: Direction) = when (direction) {
        Direction.TOP -> Coordinate(line - 1, column)
        Direction.RIGHT -> Coordinate(line, column + 1)
        Direction.BOTTOM -> Coordinate(line + 1, column)
        Direction.LEFT -> Coordinate(line, column - 1)
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Coordinate) return false
        return line == other.line && column == other.column
    }

    override fun toString() = "$line ; $column"

}

private fun roundCoordinate(coordinate: Int, maxValue: Int): Int {
    if (coordinate < 0) return maxValue
    if (coordinate > maxValue) return 0
    return coordinate
}