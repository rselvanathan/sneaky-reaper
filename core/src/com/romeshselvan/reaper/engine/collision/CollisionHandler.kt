package com.romeshselvan.reaper.engine.collision

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.romeshselvan.reaper.engine.entities.EntityBody

object CollisionHandler: ContactListener {

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        // Nothing
    }

    override fun beginContact(contact: Contact?) {
        val bodyA = contact?.fixtureA?.body
        val bodyB = contact?.fixtureB?.body
        val entityBodyA = bodyA?.userData as EntityBody
        val entityBodyB = bodyB?.userData as EntityBody
        entityBodyA.onCollision(bodyB, contact.fixtureA.userData as FixtureType)
        entityBodyB.onCollision(bodyA, contact.fixtureB.userData as FixtureType)
    }

    override fun endContact(contact: Contact?) {
        val bodyA = contact?.fixtureA?.body
        val bodyB = contact?.fixtureB?.body
        val entityBodyA = bodyA?.userData as EntityBody
        val entityBodyB = bodyB?.userData as EntityBody
        entityBodyA.onCollisionEnd(bodyB, contact.fixtureA.userData as FixtureType)
        entityBodyB.onCollisionEnd(bodyA, contact.fixtureB.userData as FixtureType)
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        // Nothing
    }
}