package com.romeshselvan.reaper.engine.input

import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.eventManager.EventManagerImpl
import com.romeshselvan.reaper.engine.input.data.State
import com.romeshselvan.reaper.engine.input.events.StatePressedEvent
import com.romeshselvan.reaper.engine.input.events.StateReleasedEvent
import com.romeshselvan.reaper.engine.input.listeners.InputStateListener
import io.kotlintest.matchers.shouldBe
import io.kotlintest.mock.`when`
import io.kotlintest.mock.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

class InputStateListenerImpl(var state: State = State("Test"),
                             var keyUp: Boolean = false,
                             var keyDown: Boolean = false) : InputStateListener {
    override fun onStateReleased(state: State) {
        this.state = state
        keyUp = true
    }

    override fun onStatePressed(state: State) {
        this.state = state
        keyDown = true
    }
}

class InputHandlerTest {

    private val EXPECTED_STATE = State("MOVE_FORWARD")

    private val eventManager: EventManager = EventManagerImpl

    private val contextManager: InputContextManager = mock()

    private lateinit var inputStateListener: InputStateListenerImpl

    private val inputHandler = InputHandler(contextManager, eventManager)

    @Before
    fun setup() {
        inputStateListener = InputStateListenerImpl()
        eventManager.addListener(inputStateListener, StatePressedEvent::class)
        eventManager.addListener(inputStateListener, StateReleasedEvent::class)
    }

    @After
    fun tearDown() {
        eventManager.clearListenerMap()
    }

    @Test
    fun `Handler should send a state pressed event to its listener when the event has been fired`() {
        inputStateListener.keyDown shouldBe false
        `when`(contextManager.getState(3)).thenReturn(EXPECTED_STATE)
        inputHandler.keyDown(3)
        inputStateListener.state shouldBe EXPECTED_STATE
        inputStateListener.keyDown shouldBe true
        inputStateListener.keyUp shouldBe false
    }

    @Test
    fun `Handler should send a state released event to its listener when the event has been fired`() {
        inputStateListener.keyUp shouldBe false
        `when`(contextManager.getState(3)).thenReturn(EXPECTED_STATE)
        inputHandler.keyUp(3)
        inputStateListener.state shouldBe EXPECTED_STATE
        inputStateListener.keyUp shouldBe true
        inputStateListener.keyDown shouldBe false
    }

    @Test
    fun `Handler should do nothing if key press down key code was not found`() {
        `when`(contextManager.getState(3)).thenReturn(null)
        inputHandler.keyDown(3)
        inputStateListener.state.stateId shouldBe "Test"
        inputStateListener.keyDown shouldBe false
    }

    @Test
    fun `Handler should do nothing if key press up key code was not found`() {
        `when`(contextManager.getState(3)).thenReturn(null)
        inputHandler.keyUp(3)
        inputStateListener.state.stateId shouldBe "Test"
        inputStateListener.keyUp shouldBe false
    }
}