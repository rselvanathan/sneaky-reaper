package com.romeshselvan.reaper.engine.screens

import com.badlogic.gdx.Screen

class ScreenManager(private var activeScreen: Screen) {

    fun changeScreen(newScreen: Screen) {
        activeScreen.dispose()
        activeScreen = newScreen
    }

    fun render(delta: Float) = activeScreen.render(delta)

    fun dispose() = activeScreen.dispose()

    fun getActiveScreen(): Screen = activeScreen
}