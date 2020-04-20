package com.example.demo.app

import com.example.demo.app.Direction.Companion.generateRandomDirection
import javafx.scene.paint.Color
import kotlin.math.abs
import kotlin.random.Random

enum class Direction {
    TOP, RIGHT, BOTTOM, LEFT;

    companion object {
        fun generateRandomDirection() = values().run { get(Random.nextInt(size)) }
    }
}

data class Cell(
        var coordinate: Coordinate,
        val cellsLocations: Array<Array<Cell?>>
) {

    var brain = Brain(100)
    var isAlive = true
    var energy = 50.0
    var age = 0
    var color = generateColorFromBrain()

    fun generateColorFromBrain(): Color {
        val red = brain.actions.filter { it is Action.Go }.count() / brain.actions.size.toDouble()
        val green = brain.actions.filter { it is Action.GenerateEnergy }.count() / brain.actions.size.toDouble()
        val blue = brain.actions.filter { it is Action.SkipActions }.count() / brain.actions.size.toDouble()
        return Color(red, green, blue, 1.0)
    }

    private var children = mutableListOf<Cell>()

    fun dropChildren(): List<Cell> =
            children.also { children = mutableListOf() }

    private fun kill() {
        isAlive = false
    }

    fun runCycle() {
        age++
        if (age >= 100) isAlive = false
        if (energy <= 0) isAlive = false
        if (!isAlive) return
        runCurrentAction(brain.getCurrentAction())
    }

    private fun runCurrentAction(action: Action) = when (action) {
        is Action.GenerateEnergy -> energy += 3//((LINES_NUMBER/2 - abs(LINES_NUMBER/2 - coordinate.line))/LINES_NUMBER.toDouble()*4 + (COLUMN_NUMBER/2 - abs(COLUMN_NUMBER/2 - coordinate.column))/COLUMN_NUMBER.toDouble()*4).toInt()
        //+(coordinate.line/ LINES_NUMBER.toDouble())
        is Action.CreateChild -> produceChild()
        is Action.Go -> moveTo(action.direction)
        else -> Unit
    }

    private fun produceChild() {
        if (energy >= 100) {
            val childCoordinate = coordinate.coordinateIn(generateRandomDirection())
            if (cellsLocations[childCoordinate] == null) {
                energy = 50.0
                val child = Cell(childCoordinate, cellsLocations)
                child.brain = Brain(arrayOf(*brain.actions))
                child.color = child.generateColorFromBrain()
                child.brain.mutate()
                children.add(child)
            }

        }
    }

    private fun Color.mutate() =
            Color(red.mutate(), green.mutate(), blue.mutate(), opacity.mutate())

    private fun Double.mutate() =
            (this + (Random.nextInt(3) - 1) * Random.nextDouble(0.1)).let {
                if (it <= 0) 0.1 else if (it >= 1.0) 0.9 else it
            }

    private fun moveTo(direction: Direction) = moveTo(coordinate.coordinateIn(direction))

    private fun moveTo(newCoordinate: Coordinate) {
        if (coordinate != newCoordinate)
            cellsLocations[newCoordinate]
                    ?.let { killCell(it) }
                    ?: moveToEmptyCell(newCoordinate)
    }

    private fun killCell(cell: Cell) {
        //colorEquality(cell.color, color) <= 0.4 &&
        if ( brain.brainEquality(cell.brain) <= 5) {
            val commonEnergy = energy + cell.energy
            energy = commonEnergy / 2
            cell.energy = commonEnergy / 2
            return
        }
        energy += cell.energy/2
        cell.kill()
    }

    private fun colorEquality(c1: Color, c2: Color) =
            (abs(c1.red - c2.red) +
                    abs(c1.green - c2.green) +
                    abs(c1.blue - c2.blue) +
                    abs(c1.opacity - c2.opacity)) / 4


    private fun moveToEmptyCell(newCoordinate: Coordinate) {
        energy--
        cellsLocations[coordinate] = null
        coordinate = newCoordinate
        cellsLocations[newCoordinate] = this
    }
}
