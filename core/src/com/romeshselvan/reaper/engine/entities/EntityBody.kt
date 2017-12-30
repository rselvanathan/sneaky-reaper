package com.romeshselvan.reaper.engine.entities

import com.badlogic.gdx.physics.box2d.Body
import com.romeshselvan.reaper.engine.collision.FixtureType

abstract class EntityBody(val body: Body, val maxSpeed: Float, val bodyType: EntityType) {

    init {
        initBody()
    }

    abstract fun update(delta: Float)
    abstract fun onCollision(otherBody: Body, collidedFixtureType : FixtureType)
    abstract fun onCollisionEnd(otherBody: Body, collidedFixtureType : FixtureType)

    private fun initBody() {
        body.userData = this
    }
}