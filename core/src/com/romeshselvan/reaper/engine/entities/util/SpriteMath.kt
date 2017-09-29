package com.romeshselvan.reaper.engine.entities.util

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body

object SpriteMath {
    fun getSpritePositionX(body: Body, sprite: Sprite): Float = body.position.x-sprite.width/2
    fun getSpritePositionY(body: Body, sprite: Sprite): Float = body.position.y-sprite.height/2
}