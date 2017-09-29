package com.romeshselvan.reaper.engine.input.events

import com.romeshselvan.reaper.engine.eventManager.Event
import com.romeshselvan.reaper.engine.input.data.State
import com.romeshselvan.reaper.engine.input.listeners.InputStateListener

class StateReleasedEvent(private val state: State): Event<InputStateListener> {
    override fun notify(listener: InputStateListener) = listener.onStateReleased(state)
}