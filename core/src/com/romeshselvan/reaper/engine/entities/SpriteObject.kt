package com.romeshselvan.reaper.engine.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite

abstract class SpriteObject(val sprite: Sprite) {

    abstract fun render(delta: Float, batch: Batch, xPos: Float, yPos: Float)

    fun dispose() = sprite.texture.dispose()
}