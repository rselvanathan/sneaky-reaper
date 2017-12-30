package com.romeshselvan.reaper.entities.bodies

import box2dLight.ConeLight
import com.badlogic.gdx.physics.box2d.Body
import com.romeshselvan.reaper.engine.ai.AISteeringBehaviour
import com.romeshselvan.reaper.engine.ai.Arrive
import com.romeshselvan.reaper.engine.ai.Seek
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.entities.EntityTypes

class KnightBody(body: Body,
                 targetEntity: EntityBody,
                 private val coneLight: ConeLight)
    : EntityBody(body, 100.0f, EntityTypes.Knight) {

    private val arriveSteering: AISteeringBehaviour = Arrive(body, targetEntity.body.position, 100.0f, 3.0f, maxSpeed)
    private val seekSteering: AISteeringBehaviour = Seek(body,  targetEntity.body.position, 50.0f, maxSpeed)

    private var enableArriveSteering = false

    override fun update(delta: Float) {
        if(enableArriveSteering) {
            arriveSteering.act()
        }
        coneLight.position = body.position
    }

    override fun onCollision(otherBody: Body, collidedFixtureType: FixtureType) {
        if(isAIChaseCheck(collidedFixtureType) && isPlayer(otherBody)) {
            enableArriveSteering = true
        }
    }

    override fun onCollisionEnd(otherBody: Body, collidedFixtureType: FixtureType) {
        if(isAIChaseCheck(collidedFixtureType) && isPlayer(otherBody)) {
            enableArriveSteering = false
            arriveSteering.stop()
        }
    }

    private fun isPlayer(otherBody: Body) : Boolean = (otherBody.userData as EntityBody).bodyType == EntityTypes.Player
    private fun isAIChaseCheck(fixtureType: FixtureType) : Boolean = fixtureType == FixtureType.AI_CHASE_CHECK
}
