package com.example.demo.view

import com.example.demo.app.Cell
import javafx.scene.paint.Color
import tornadofx.*
import java.lang.Thread.sleep
import kotlin.random.Random


const val FULL_HEIGHT = 600.0
const val FULL_WIDTH = 600.0

const val LINES_NUMBER = 50
const val COLUMN_NUMBER = 50

class MainView : View("Hello TornadoFX") {
    private val emptyPixelColor = Color(1.0, 1.0, 1.0, 1.0)

    private val pixelsColorsMatrix = Array(LINES_NUMBER) {
        Array(COLUMN_NUMBER) {
            emptyPixelColor.toProperty()
        }
    }

    private val cellsLocations = Array(LINES_NUMBER) {
        Array<Cell?>(COLUMN_NUMBER) {
            null
        }
    }

    override val root = pane {
        for (lineNum in 0 until LINES_NUMBER) {
            for (columnNum in 0 until COLUMN_NUMBER) {
                rectangle {
                    width = FULL_WIDTH / COLUMN_NUMBER
                    height = FULL_HEIGHT / LINES_NUMBER
                    x = columnNum * width
                    y = lineNum * height

                    fillProperty().bind(pixelsColorsMatrix[lineNum][columnNum])
                }
            }
        }
        runAsync {
            val cells = List(2500) { createDefaultCell() }
            while (true) {
                cells.forEach { it.runCycle() }
                runLater {
                    clearPixels()
                    drawCells(cells)
                }
                sleep(25)
            }
        }
    }

    private fun createDefaultCell() =
            createCell(LINES_NUMBER / 2, COLUMN_NUMBER / 2, generateRandomColor())

    private fun createCell(lineNumber: Int, columnNumber: Int, color: Color) =
            Cell(lineNumber, columnNumber, color, cellsLocations)
                    .also { cellsLocations[lineNumber][columnNumber] = it }

    private fun drawCells(cells: List<Cell>) = cells.forEach(::drawCell)

    private fun drawCell(cell: Cell) {
        pixelsColorsMatrix[cell.lineNumber][cell.columnNumber].value = cell.color
    }

    private fun clearPixels() =
            pixelsColorsMatrix.forEach{ it.forEach { it.value = emptyPixelColor } }

    private fun generateRandomColor() =
            Color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), 1.0)
}