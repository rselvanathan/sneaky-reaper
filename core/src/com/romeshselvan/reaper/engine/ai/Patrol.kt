package com.romeshselvan.reaper.engine.ai

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

/**
 * Patrol behaviour of an AI body, that will walk between a predefined list of destinations
 *
 * @author Romesh Selvan
 */
class Patrol(private val aiBody: Body,
             private val destinationList: List<Vector2>,
             private val arriveDistance: Float,
             private val speed: Float) : AIBaseSteering, AISteeringBehaviour {

    private var currentDestinationIterator = 0
    private var currentDestination: Vector2 = destinationList.first()

    override fun act() {
        if(hasReachedDestination(aiBody.position, currentDestination, arriveDistance)) {
            currentDestinationIterator = circularIncrementAndGet(currentDestinationIterator, destinationList.size-1)
            currentDestination = destinationList[currentDestinationIterator]
        }
        val directionVector = getDirectionVector(aiBody.position, currentDestination)
        val distanceToTarget = directionVector.len()
        directionVector.nor()
        aiBody.linearVelocity = getVelocity(directionVector, speed, distanceToTarget, arriveDistance)
    }

    override fun stop() {
        aiBody.linearVelocity = Vector2(0.0f, 0.0f)
    }

    private fun circularIncrementAndGet(value: Int, ceiling: Int) : Int =
            if (value == ceiling) 0 else value + 1
    private fun hasReachedDestination(currentPos: Vector2, destination: Vector2, arriveDistance: Float) : Boolean =
            getDirectionVector(currentPos, destination).len() <= arriveDistance
}
