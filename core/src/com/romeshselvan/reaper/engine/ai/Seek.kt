package com.romeshselvan.reaper.engine.ai

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * A Behaviour which will force the AI body to move towards it's target position at a constant speed and stop once it
 * reaches a certain distance from the target position.
 *
 * @author Romesh Selvanathan
 */
class Seek(private val aiBody: Body,
           private val targetPosition: Vector2,
           private val distanceToStop: Float,
           private val speed: Float)
    : AISteeringBehaviour, AIBaseSteering {

    override fun act() {
        val directionVector = getDirectionVector(aiBody.position, targetPosition)
        val distanceToTarget = directionVector.len()
        directionVector.nor()
        aiBody.linearVelocity = getVelocity(directionVector, speed, distanceToTarget, distanceToStop)
    }

    override fun stop() {
        aiBody.linearVelocity = Vector2(0.0f, 0.0f)
    }
}
