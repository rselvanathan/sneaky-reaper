package com.romeshselvan.reaper.entities.builders

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.entityBuilders.EntityBodyBuilderUtil
import com.romeshselvan.reaper.engine.entityBuilders.EntityBuilder
import com.romeshselvan.reaper.entities.WallEntity
import com.romeshselvan.reaper.entities.bodies.WallBody

class WallBuilder(private val world: World,
                  private val builderUtil: EntityBodyBuilderUtil) : EntityBuilder {


    override fun build(mapObject: MapObject): Entity {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        val width = mapObject.properties["width", Float::class.java]/2
        val height = mapObject.properties["height", Float::class.java]/2
        return makeWall(world, x + width, y + height, width, height)
    }

    private fun makeWall(world: World, xPos: Float, yPos: Float, width: Float, height: Float): WallEntity {
        val staticBody = builderUtil.getStaticBody(xPos, yPos)
        val mainBody = world.createBody(staticBody)
        val boxPolygonShape = builderUtil.getBoxPolygonShape(width, height)
        val defaultFixtureDef = builderUtil.getDefaultFixtureDef(boxPolygonShape)
        mainBody.createFixture(defaultFixtureDef).userData = FixtureType.DEFAULT_BODY
        boxPolygonShape.dispose()
        return WallEntity(WallBody(mainBody))
    }
}