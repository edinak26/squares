package com.example.demo.app

import kotlin.random.Random

sealed class Action {
    data class Go(val direction: Direction) : Action()
    object GenerateEnergy : Action()
    object CreateChild : Action()
    object Wait : Action()
    data class SkipActions(val actionsNum: Int) : Action()

    companion object{
        fun generateRandomAction(): Action = when (Random.nextInt(7)) {
            0 -> GenerateEnergy
            1 -> CreateChild
            2 -> SkipActions(Random.nextInt(100))
            100 -> Wait
            else -> Go(Direction.generateRandomDirection())
        }
    }
}