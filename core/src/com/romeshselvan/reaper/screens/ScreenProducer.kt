package com.romeshselvan.reaper.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.romeshselvan.reaper.GameObjectProducer
import com.romeshselvan.reaper.GameWorld
import com.romeshselvan.reaper.defaults.ScreenType
import com.romeshselvan.reaper.engine.collision.CollisionHandler
import com.romeshselvan.reaper.engine.eventManager.EventManager

object ScreenProducer {

    fun getScreen(screenType: ScreenType, spriteBatch: SpriteBatch): Screen = when(screenType) {
        ScreenType.GAME -> GameScreen(spriteBatch, getGameWorld(), EventManager.getEventManager())
        else -> throw IllegalArgumentException("$screenType is not supported")
    }

    private fun getGameWorld() =
            GameWorld.getInstance(GameObjectProducer.getGameObjectProducer(), CollisionHandler, EventManager.getEventManager(),
                    OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))
}