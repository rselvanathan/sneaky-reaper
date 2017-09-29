package com.romeshselvan.reaper.entities

import com.romeshselvan.reaper.engine.entities.EntityType

object EntityTypes {

    private var counter: Int = 0
    private fun getId(): Int = counter.plus(1)

    object Player : EntityType(getId())
    object Wall : EntityType(getId())
    object Knight : EntityType(getId())
}