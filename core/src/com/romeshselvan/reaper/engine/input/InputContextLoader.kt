package com.romeshselvan.reaper.engine.input

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.romeshselvan.reaper.engine.input.data.Action
import com.romeshselvan.reaper.engine.input.data.InputContext
import com.romeshselvan.reaper.engine.input.data.State

interface InputContextLoader {
    fun loadInputContexts(rawJson: String): List<InputContext>

    companion object {
        fun getInputContextLoader(): InputContextLoader = InputContextLoaderImpl
    }
}

private object InputContextLoaderImpl: InputContextLoader {
    /** JSON Data mapping helper classes **/
    private data class JsonAction(val key: Int, val action: String)
    private data class JsonState(val key: Int, val state: String)
    private data class Context(val contextName: String, val actions: List<JsonAction>, val states: List<JsonState>)
    private data class ContextCollection(val contexts: List<Context>)

    private val objectMapper = jacksonObjectMapper()

    override fun loadInputContexts(rawJson: String): List<InputContext> {
        val contextCollection = objectMapper.readValue(rawJson, ContextCollection::class.java)
        return contextCollection.contexts.map {
            val actionMap = it.actions.associate { Pair(it.key, Action(it.action)) }
            val stateMap = it.states.associate { Pair(it.key, State(it.state)) }
            InputContext(it.contextName, actionMap, stateMap)
        }
    }
}