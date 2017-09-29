package com.romeshselvan.reaper.engine.input.data

data class InputContext(val contextName: String,
                        private val keyToActionMap: Map<Int, Action>,
                        private val keyToStateMap: Map<Int, State>) {

    fun getAction(key: Int): Action? = keyToActionMap[key]
    fun getState(key: Int): State? = keyToStateMap[key]
}