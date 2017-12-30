package com.romeshselvan.reaper.engine.ai

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Arrive(private val aiBody: Body,
             private val targetPosition: Vector2,
             private val arriveDistance: Float,
             private val decelerationValue: Float,
             private val maxSpeed: Float): AIBaseSteering {


    override fun act() {
        val directionVector = getDirectionVector(aiBody.position, targetPosition)
        val distance = directionVector.len()
        val speed = calculateSpeed(distance/decelerationValue, maxSpeed)
        aiBody.linearVelocity = getVelocity(directionVector, (speed/distance), distance, arriveDistance)
    }

    override fun stop() {
        aiBody.linearVelocity = Vector2(0.0f, 0.0f)
    }

    private fun calculateSpeed(uncappedSpeed: Float, maxSpeed: Float): Float =
            if(uncappedSpeed > maxSpeed) maxSpeed else uncappedSpeed
}