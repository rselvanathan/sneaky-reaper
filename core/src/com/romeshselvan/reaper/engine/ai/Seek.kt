package com.romeshselvan.reaper.engine.ai

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Seek(private val aiBody: Body,
           private val targetPosition: Vector2,
           private val distanceToStop: Float,
           private val speed: Float)
    : AIBaseSteering {

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
