package com.romeshselvan.reaper.entities.bodies

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.romeshselvan.reaper.defaults.InputStates
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.engine.input.data.State
import com.romeshselvan.reaper.engine.input.listeners.InputStateListener
import com.romeshselvan.reaper.entities.EntityTypes

class ReaperBody(body: Body, private val camera: OrthographicCamera): EntityBody(body, 120.0f, EntityTypes.Player), InputStateListener {

    private val additiveVelocity: Vector2 = Vector2(0.0f, 0.0f)

    override fun update(delta: Float) {
        setVelocity()
        camera.position.set(body.position.x, body.position.y, 0.0f)
    }

    override fun onStatePressed(state: State) {
        when(state.stateId) {
            InputStates.MOVE_UP.name -> additiveVelocity.y += maxSpeed
            InputStates.MOVE_DOWN.name -> additiveVelocity.y += maxSpeed * -1.0f
            InputStates.MOVE_LEFT.name -> additiveVelocity.x += maxSpeed * -1.0f
            InputStates.MOVE_RIGHT.name -> additiveVelocity.x += maxSpeed
        }
    }

    override fun onStateReleased(state: State) {
        when(state.stateId) {
            InputStates.MOVE_UP.name -> additiveVelocity.y += maxSpeed * -1.0f
            InputStates.MOVE_DOWN.name -> additiveVelocity.y += maxSpeed
            InputStates.MOVE_LEFT.name -> additiveVelocity.x += maxSpeed
            InputStates.MOVE_RIGHT.name -> additiveVelocity.x += maxSpeed * -1.0f
        }
    }

    override fun onCollision(otherBody: Body, collidedFixtureType: FixtureType) {
        // Do Nothing
    }

    override fun onCollisionEnd(otherBody: Body, collidedFixtureType: FixtureType) {
        // Do Nothing
    }

    private fun setVelocity() {
        body.linearVelocity = additiveVelocity
    }
}
