package com.romeshselvan.reaper.engine.input

import com.romeshselvan.reaper.engine.input.data.Action
import com.romeshselvan.reaper.engine.input.data.InputContext
import com.romeshselvan.reaper.engine.input.data.State

class InputContextManager(private val inputContextList: List<InputContext>) {

    private var activeContext: InputContext = inputContextList.last()

    fun changeContext(contextName: String) {
        val optionalContext = inputContextList.find { it.contextName == contextName }
        activeContext = optionalContext ?: throw IllegalArgumentException("Context $contextName not found")
    }

    fun getAction(key: Int): Action? = activeContext.getAction(key)

    fun getState(key: Int): State? = activeContext.getState(key)

    fun getActiveContext(): InputContext = activeContext
}