package com.romeshselvan.reaper.engine.input.listeners

import com.romeshselvan.reaper.engine.input.data.State

interface InputStateListener {
    fun onStatePressed(state: State)
    fun onStateReleased(state: State)
}