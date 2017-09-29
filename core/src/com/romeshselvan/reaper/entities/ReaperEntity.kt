package com.romeshselvan.reaper.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.entities.util.SpriteMath
import com.romeshselvan.reaper.entities.bodies.ReaperBody
import com.romeshselvan.reaper.entities.sprites.ReaperSprite

class ReaperEntity(body: ReaperBody, sprite: ReaperSprite): Entity(body, sprite) {

    override fun render(delta: Float, batch: Batch) {
        val nonNullSprite = spriteObject ?: throw IllegalArgumentException("Reaper sprite cannot be null")
        nonNullSprite.render(delta, batch,
                SpriteMath.getSpritePositionX(body.body, nonNullSprite.sprite),
                SpriteMath.getSpritePositionY(body.body, nonNullSprite.sprite))
    }

    override fun update(delta: Float) {
        body.update(delta)
    }
}