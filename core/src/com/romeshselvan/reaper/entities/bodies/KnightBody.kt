package com.romeshselvan.reaper.entities.bodies

import box2dLight.ConeLight
import com.badlogic.gdx.physics.box2d.Body
import com.romeshselvan.reaper.engine.ai.AISteeringBehaviour
import com.romeshselvan.reaper.engine.ai.Arrive
import com.romeshselvan.reaper.engine.ai.Seek
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.entities.EntityTypes

class KnightBody(body: Body,
                 targetEntity: EntityBody,
                 private val coneLight: ConeLight)
    : EntityBody(body, 100.0f, EntityTypes.Knight) {

    private val arriveSteering: AISteeringBehaviour = Arrive(body, targetEntity.body.position, 100.0f, 3.0f, maxSpeed)
    private val seekSteering: AISteeringBehaviour = Seek(body,  targetEntity.body.position, 50.0f, maxSpeed)

    override fun update(delta: Float) {
        arriveSteering.act()
        coneLight.position = body.position
    }

    override fun onCollision(otherBody: Body) {
        // Do Nothing
    }

    override fun onCollisionEnd(otherBody: Body) {
        // Do Nothing
    }

}