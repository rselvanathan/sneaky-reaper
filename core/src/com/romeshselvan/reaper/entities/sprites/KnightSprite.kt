package com.romeshselvan.reaper.entities.sprites

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.romeshselvan.reaper.engine.entities.SpriteObject

class KnightSprite(sprite: Sprite): SpriteObject(sprite) {

    override fun render(delta: Float, batch: Batch, xPos: Float, yPos: Float) {
        batch.begin()
        sprite.setPosition(xPos, yPos)
        sprite.draw(batch)
        batch.end()
    }
}