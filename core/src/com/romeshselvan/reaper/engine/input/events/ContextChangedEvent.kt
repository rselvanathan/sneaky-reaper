package com.romeshselvan.reaper.engine.input.events

import com.romeshselvan.reaper.engine.eventManager.Event
import com.romeshselvan.reaper.engine.input.listeners.ContextChangeListener

class ContextChangedEvent(private val contextName: String): Event<ContextChangeListener> {

    override fun notify(listener: ContextChangeListener) = listener.onContextChanged(contextName)
}