package com.romeshselvan.reaper.engine.input

import com.romeshselvan.reaper.engine.input.data.InputContext
import io.kotlintest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class InputContextManagerTest {

    private val GAME = "game"
    private val MENU = "menu"

    private val menuContext = defaultContextName(MENU)
    private val gameContext = defaultContextName(GAME)

    lateinit var contextManager: InputContextManager

    @Before
    fun setup() {
        contextManager = InputContextManager(listOf(gameContext, menuContext))
    }

    @Test
    fun `Manager should change context successfully when it finds the context name in the list`() {
        contextManager.getActiveContext() shouldBe menuContext
        contextManager.changeContext(GAME)
        contextManager.getActiveContext() shouldBe gameContext
    }

    @Test
    fun `Manager should throw IllegalArgument exception with expected message when context is not found`() {
        contextManager.getActiveContext() shouldBe menuContext
        assertFailsWith<IllegalArgumentException> { contextManager.changeContext("RANDOM") }
    }

    private fun defaultContextName(name: String) = InputContext(name, emptyMap(), emptyMap())
}