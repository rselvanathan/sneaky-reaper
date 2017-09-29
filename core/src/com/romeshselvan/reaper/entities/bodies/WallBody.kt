package com.romeshselvan.reaper.entities.bodies

import com.badlogic.gdx.physics.box2d.Body
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.entities.EntityTypes

class WallBody(body: Body): EntityBody(body, 0.0f, EntityTypes.Wall) {
    override fun update(delta: Float) {
        // Do Nothing
    }

    override fun onCollision(otherBody: Body) {
        // Do Nothing
    }

    override fun onCollisionEnd(otherBody: Body) {
        // Do Nothing
    }
}