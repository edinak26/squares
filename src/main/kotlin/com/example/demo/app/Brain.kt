package com.example.demo.app

import com.example.demo.app.Action.Companion.generateRandomAction
import kotlin.random.Random

class Brain(val actions: Array<Action>) {
    constructor(actionsNumber: Int) : this(Array(actionsNumber) { generateRandomAction() })

    var currentActionIndex = 0

    fun getCurrentAction(recursiveNum : Int = 0): Action {
        if(recursiveNum>1) return Action.Wait
        val action = actions[(currentActionIndex++) % actions.size]
        return if(action is Action.SkipActions) {currentActionIndex+=action.actionsNum; getCurrentAction(recursiveNum+1)} else {action}
    }

    fun mutate() {
        if(Random.nextInt(4) == 0)
            actions[Random.nextInt(actions.size)] = generateRandomAction()
    }

    fun brainEquality(brain: Brain):Int{
        var notEqualActions = 0
        actions.forEachIndexed { index, action -> if(brain.actions[index]!=action)notEqualActions++  }
        return notEqualActions
    }

    companion object{
        fun getDefaultBrain() = Brain(Array<Action>(20){Action.GenerateEnergy}.apply { this[19] = Action.CreateChild }

        )
    }

}