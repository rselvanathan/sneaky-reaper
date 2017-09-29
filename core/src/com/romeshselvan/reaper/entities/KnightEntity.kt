package com.romeshselvan.reaper.entities

import com.badlogic.gdx.graphics.g2d.Batch
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.entities.util.SpriteMath
import com.romeshselvan.reaper.entities.bodies.KnightBody
import com.romeshselvan.reaper.entities.sprites.KnightSprite

class KnightEntity(entityBody: KnightBody, sprite: KnightSprite): Entity(entityBody, sprite) {

    override fun render(delta: Float, batch: Batch) {
        val nonNullSprite = spriteObject ?: throw IllegalArgumentException("Knight sprite cannot be null")
        nonNullSprite.render(delta, batch,
                SpriteMath.getSpritePositionX(body.body, nonNullSprite.sprite),
                SpriteMath.getSpritePositionY(body.body, nonNullSprite.sprite))
    }

    override fun update(delta: Float) {
        body.update(delta)
    }

}