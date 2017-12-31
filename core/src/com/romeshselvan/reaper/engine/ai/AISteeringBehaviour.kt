package com.romeshselvan.reaper.engine.ai

/**
 * A Steering Behaviour for an AI EntityBody.
 *
 * @author Romesh Selvanathan
 */
interface AISteeringBehaviour {

    /**
     * Act out the behaviour defined. This method should be called at every "update".
     */
    fun act()

    /**
     * Reset the forces applied on the EntityBody. This should always be called once the behaviour is expected to be
     * stopped.
     */
    fun stop()
}