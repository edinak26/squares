package com.example.demo.app

import com.example.demo.view.COLUMN_NUMBER
import com.example.demo.view.LINES_NUMBER
import javafx.scene.paint.Color
import kotlin.random.Random

enum class Direction {
    TOP, RIGHT, BOTTOM, LEFT
}

class Cell(
        var lineNumber: Int,
        var columnNumber: Int,
        var color: Color,
        private val cellsLocations: Array<Array<Cell?>>
) {
    fun runCycle() =
            moveTo(Direction.values().run { get(Random.nextInt(size)) })


    private fun moveTo(direction: Direction) = when (direction) {
        Direction.TOP -> moveTo(lineNumber - 1, columnNumber)
        Direction.RIGHT -> moveTo(lineNumber, columnNumber + 1)
        Direction.BOTTOM -> moveTo(lineNumber + 1, columnNumber)
        Direction.LEFT -> moveTo(lineNumber, columnNumber - 1)
    }

    private fun moveTo(newLine: Int, newColumn: Int) {
        val roundNewLine = roundCoordinate(newLine, LINES_NUMBER - 1)
        val roundNewColumn = roundCoordinate(newColumn, COLUMN_NUMBER - 1)
        if (cellsLocations[roundNewLine][roundNewColumn] != null) return

        cellsLocations[lineNumber][columnNumber] = null
        lineNumber = roundNewLine
        columnNumber = roundNewColumn
        cellsLocations[lineNumber][columnNumber] = this
    }

    private fun roundCoordinate(coordinate: Int, maxValue: Int): Int {
        if (coordinate < 0) return 0
        if (coordinate > maxValue) return maxValue
        return coordinate
    }
}
