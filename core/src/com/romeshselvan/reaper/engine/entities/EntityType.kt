package com.romeshselvan.reaper.engine.entities

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