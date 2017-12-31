package com.romeshselvan.reaper.engine.entities.util

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body

/**
 * Common Sprite specific maths
 *
 * @author Romesh Selvanathan
 */
object SpriteMath {
    /**
     * Retrieve the screen X position of the sprite, by extracting the value from the "physics" world position
     */
    fun getSpritePositionX(body: Body, sprite: Sprite): Float = body.position.x-sprite.width/2
    /**
     * Retrieve the screen Y position of the sprite, by extracting the value from the "physics" world position
     */
    fun getSpritePositionY(body: Body, sprite: Sprite): Float = body.position.y-sprite.height/2
}