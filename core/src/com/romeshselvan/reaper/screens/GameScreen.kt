package com.romeshselvan.reaper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.romeshselvan.reaper.GameWorld
import com.romeshselvan.reaper.defaults.InputContextType.GAME_CONTEXT
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.input.events.ContextChangedEvent

class GameScreen(private val spriteBatch: SpriteBatch,
                 private val gameWorld: GameWorld,
                 eventManager: EventManager): Screen {

    init {
        eventManager.notifyListeners(ContextChangedEvent(GAME_CONTEXT.contextName))
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameWorld.update(delta)
        gameWorld.render(delta, spriteBatch)
    }

    override fun dispose() {
        gameWorld.dispose()
    }

    override fun resume() {
        TODO("not implemented")
    }

    override fun hide() {
        TODO("not implemented")
    }

    override fun show() {
        TODO("not implemented")
    }

    override fun pause() {
        TODO("not implemented")
    }

    override fun resize(width: Int, height: Int) {
        TODO("not implemented")
    }
}