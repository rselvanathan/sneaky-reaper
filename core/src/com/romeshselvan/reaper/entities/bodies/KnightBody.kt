package com.romeshselvan.reaper.entities.bodies

import box2dLight.ConeLight
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.RayCastCallback
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.engine.ai.AISteeringBehaviour
import com.romeshselvan.reaper.engine.ai.Arrive
import com.romeshselvan.reaper.engine.ai.Patrol
import com.romeshselvan.reaper.engine.ai.Seek
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.entities.EntityTypes

class KnightBody(body: Body,
                 targetEntity: EntityBody,
                 patrolDestination: List<Vector2>,
                 private val world: World,
                 private val coneLight: ConeLight)
    : EntityBody(body, 100.0f, EntityTypes.Knight), RayCastCallback {

    private val arriveSteering: AISteeringBehaviour =
            Arrive(body, targetEntity.body.position, 100.0f, 3.0f, maxSpeed)
    private val seekSteering: AISteeringBehaviour =
            Seek(body,  targetEntity.body.position, 50.0f, maxSpeed)
    private val patrolSteering: AISteeringBehaviour =
            Patrol(body, patrolDestination, 5.0f, maxSpeed*0.75f)

    private var aiSteering: AISteeringBehaviour = patrolSteering

    override fun update(delta: Float) {
        aiSteering.act()
        coneLight.position = body.position
    }

    override fun onCollision(otherBody: Body, collidedFixtureType: FixtureType) {
        if(isSensor(collidedFixtureType) && isPlayer(otherBody)) {
            world.rayCast(this, body.position, otherBody.position)
        }
    }

    override fun onCollisionEnd(otherBody: Body, collidedFixtureType: FixtureType) {
        if(isInnerSensor(collidedFixtureType) && isPlayer(otherBody)) {
            world.rayCast(this, body.position, otherBody.position)
        } else if(isOuterSensor(collidedFixtureType) && isPlayer(otherBody)) {
            aiSteering = patrolSteering
        }
    }

    override fun reportRayFixture(fixture: Fixture?, point: Vector2?, normal: Vector2?, fraction: Float): Float {
        if(!isPlayer(fixture!!)) {
            if(aiSteering == arriveSteering) {
                aiSteering = patrolSteering
            }
            return 0.0f
        }
        aiSteering = arriveSteering
        return -1.0f
    }

    private fun isPlayer(otherBody: Body) : Boolean = (otherBody.userData as EntityBody).bodyType == EntityTypes.Player
    private fun isPlayer(collidedFixture : Fixture) : Boolean =
            (collidedFixture.body!!.userData as EntityBody).bodyType == EntityTypes.Player
    private fun isInnerSensor(fixtureType: FixtureType) : Boolean = fixtureType == FixtureType.AI_CHASE_CHECK
    private fun isOuterSensor(fixtureType: FixtureType) : Boolean = fixtureType == FixtureType.AI_OUTER_CHASE_CHECK
    private fun isSensor(fixtureType: FixtureType) : Boolean = isInnerSensor(fixtureType) || isOuterSensor(fixtureType)
}
