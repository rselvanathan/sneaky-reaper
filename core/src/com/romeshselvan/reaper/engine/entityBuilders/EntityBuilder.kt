package com.romeshselvan.reaper.engine.entityBuilders

import com.badlogic.gdx.maps.MapObject
import com.romeshselvan.reaper.engine.entities.Entity

/**
 * Build a entity object for the game
 *
 * @author Romesh Selvanathan
 */
interface EntityBuilder {

    /**
     * Build and return an Entity
     *
     * @param mapObject The TiledMap map object containing data to help generate the Entity
     * @return an Entity object
     */
    fun build(mapObject: MapObject) : Entity
}