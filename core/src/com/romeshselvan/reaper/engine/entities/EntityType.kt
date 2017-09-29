package com.romeshselvan.reaper.engine.entities

abstract class EntityType(private val id: Int) {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            other is EntityType -> (other as EntityType).id == this.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id
    }
}