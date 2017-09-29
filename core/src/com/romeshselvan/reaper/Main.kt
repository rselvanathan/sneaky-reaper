package com.romeshselvan.reaper

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.romeshselvan.reaper.defaults.ScreenType
import com.romeshselvan.reaper.engine.collision.CollisionHandler
import com.romeshselvan.reaper.engine.screens.ScreenManager
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.input.InputContextLoader
import com.romeshselvan.reaper.engine.input.InputContextManager
import com.romeshselvan.reaper.engine.input.InputHandler
import com.romeshselvan.reaper.screens.GameScreen
import com.romeshselvan.reaper.screens.ScreenProducer
import java.io.InputStreamReader

class Main : ApplicationAdapter() {
    private lateinit var spriteBatch : SpriteBatch

    private lateinit var screenManager: ScreenManager
    private lateinit var inputHandler: InputHandler
    private val eventManager: EventManager = EventManager.getEventManager()
    private val inputContextLoader: InputContextLoader = InputContextLoader.getInputContextLoader()

    override fun create() {
        spriteBatch = SpriteBatch()
        inputHandler = InputHandler(InputContextManager(inputContextLoader.loadInputContexts(getInputJsonString())),
                                                        eventManager)
        Gdx.input.inputProcessor = inputHandler

        screenManager = ScreenManager(ScreenProducer.getScreen(ScreenType.GAME, spriteBatch))
    }

    override fun render() {
        screenManager.render(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        spriteBatch.dispose()
        eventManager.clearListenerMap()
    }

    private fun getInputJsonString(): String {
        val inputStreamReader = InputStreamReader(Gdx.files.internal("input.json").read())
        val inputJsonString = inputStreamReader.readText()
        inputStreamReader.close()
        return inputJsonString
    }
}