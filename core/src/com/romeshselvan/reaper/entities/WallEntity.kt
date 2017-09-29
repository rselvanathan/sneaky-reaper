package com.romeshselvan.reaper.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.entities.bodies.WallBody

class WallEntity(entityBody: WallBody): Entity(entityBody, null) {

    override fun render(delta: Float, batch: Batch) {
        // Do Nothing
    }

    override fun update(delta: Float) {
        // Do Nothing
    }
}