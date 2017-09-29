package com.romeshselvan.reaper.engine.ai

import com.badlogic.gdx.math.Vector2

abstract class AIBaseSteering: AISteeringBehaviour {

    protected fun getDirectionVector(fromVector: Vector2, targetVector: Vector2) =
            Vector2(targetVector.x - fromVector.x, targetVector.y - fromVector.y)

    protected fun getVelocity(directionVector2: Vector2, speed: Float, distanceToTarget: Float, distanceToStop: Float) =
            if(distanceToTarget > distanceToStop) Vector2(directionVector2.x * speed, directionVector2.y * speed)
            else Vector2(0.0f, 0.0f)
}