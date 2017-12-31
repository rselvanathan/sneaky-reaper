package com.romeshselvan.reaper.engine.entities

/**
 * Represents a Entity Type with a unique ID. This can be used to determine what type of objects have collided.
 *
 * @author Romesh Selvanathan
 */
abstract class EntityType(private val id: Int) {

    override fun equals(other: Any?): Boolean {
        if(other is EntityType) {
            return other.id == this.id
        }
        return false
    }

    override fun hashCode(): Int {
        return id
    }
}