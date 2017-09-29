package com.romeshselvan.reaper.engine.input

import com.badlogic.gdx.InputProcessor
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.input.events.ContextChangedEvent
import com.romeshselvan.reaper.engine.input.events.StatePressedEvent
import com.romeshselvan.reaper.engine.input.events.StateReleasedEvent
import com.romeshselvan.reaper.engine.input.listeners.ContextChangeListener

class InputHandler(private val contextManager: InputContextManager, private val eventManager: EventManager)
    : InputProcessor, ContextChangeListener {

    init {
        eventManager.addListener(this, ContextChangedEvent::class)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = true

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = true

    override fun scrolled(amount: Int): Boolean = true

    override fun keyTyped(character: Char): Boolean = true

    override fun keyUp(keycode: Int): Boolean {
        contextManager.getState(keycode)?.apply { eventManager.notifyListeners(StateReleasedEvent(this)) }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        contextManager.getState(keycode)?.apply { eventManager.notifyListeners(StatePressedEvent(this)) }
        return true
    }

    override fun onContextChanged(contextName: String) = contextManager.changeContext(contextName)
}