package com.romeshselvan.reaper.engine.eventManager

interface Event<in T> {
    fun notify(listener: T)
}