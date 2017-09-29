package com.romeshselvan.reaper.engine.entities

import com.badlogic.gdx.physics.box2d.Body

abstract class EntityBody(val body: Body, val maxSpeed: Float, private val bodyType: EntityType) {

    init {
        body.userData = this
    }

    abstract fun update(delta: Float)
    abstract fun onCollision(otherBody: Body)
    abstract fun onCollisionEnd(otherBody: Body)

    override fun hashCode(): Int = bodyType.hashCode()
}