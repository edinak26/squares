package com.example.demo.app

import javafx.scene.paint.Color
import tornadofx.*
import java.lang.Thread.sleep
import kotlin.random.Random


const val FULL_HEIGHT = 700.0
const val FULL_WIDTH = 700.0

const val LINES_NUMBER = 100
const val COLUMN_NUMBER = 100

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
            val cells = MutableList(1) { generateRandomCell() }
            cells.forEach { it.brain = Brain.getDefaultBrain() }
            cells.forEach { it.color = it.generateColorFromBrain() }
            while (true) {
                val newCells = mutableListOf<Cell>()
                runLater {
                    clearPixels()
                    cells.forEach {
                        it.runCycle()
                        val newChildren = it.dropChildren()
                        newChildren.forEach(::addCell)
                        newCells.addAll(newChildren)
                    }
                    cells.addAll(newCells)
                    val deadCells = cells.filterNot { it.isAlive }
                    deadCells.forEach(::removeCell)
                    cells.removeIf { !it.isAlive }
                    drawCells(cells)
                }
                sleep(2L+cells.size/1000L)
            }
        }
    }

    private fun generateRandomCell() =
            createCell(generateRandomCoordinate())

    private fun createCell(coordinate: Coordinate) =
            Cell(coordinate, cellsLocations)
                    .also (::addCell)

    private fun addCell(cell: Cell) {
        cellsLocations[cell.coordinate] = cell
    }

    private fun removeCell(cell: Cell) {
        cellsLocations[cell.coordinate] = null
    }

    private fun drawCells(cells: List<Cell>) = cells.forEach(::drawCell)

    private fun drawCell(cell: Cell) {
        pixelsColorsMatrix[cell.coordinate].value = cell.color
    }

    private fun clearCell(cell: Cell) {
        pixelsColorsMatrix[cell.coordinate].value = emptyPixelColor
    }

    private fun clearPixels() = pixelsColorsMatrix.forEach { it.forEach { it.value = emptyPixelColor } }


    private fun generateRandomColor() =
            Color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble(), 1.0)
}