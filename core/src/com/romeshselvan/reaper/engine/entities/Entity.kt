package com.romeshselvan.reaper.engine.entities

import com.badlogic.gdx.graphics.g2d.Batch

abstract class Entity(val body: EntityBody, val spriteObject: SpriteObject?) {

    fun dispose() = spriteObject?.dispose()

    abstract fun render(delta: Float, batch: Batch)
    abstract fun update(delta: Float)
}