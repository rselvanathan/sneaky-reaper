package com.romeshselvan.reaper.entities.builders

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.assets.ReaperTextures
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.entityBuilders.EntityBodyBuilderUtil
import com.romeshselvan.reaper.engine.entityBuilders.EntityBuilder
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.input.events.StatePressedEvent
import com.romeshselvan.reaper.engine.input.events.StateReleasedEvent
import com.romeshselvan.reaper.entities.ReaperEntity
import com.romeshselvan.reaper.entities.bodies.ReaperBody
import com.romeshselvan.reaper.entities.sprites.ReaperSprite

class ReaperBuilder(private val world: World,
                    private val builderUtil: EntityBodyBuilderUtil,
                    private val camera: OrthographicCamera,
                    private val eventManager: EventManager) : EntityBuilder {

    override fun build(mapObject: MapObject): Entity {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        return makeReaper(world, x, y, camera, eventManager)
    }

    private fun makeReaper(world: World, xPos: Float, yPos: Float, camera: OrthographicCamera, eventManager: EventManager): ReaperEntity {
        val mainBody = world.createBody(builderUtil.getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(ReaperTextures.upFacingSet[0])
        val boxPolygonShape = builderUtil.getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val fixtureDef = builderUtil.getDefaultFixtureDef(boxPolygonShape)

        mainBody.createFixture(fixtureDef).userData = FixtureType.DEFAULT_BODY
        boxPolygonShape.dispose()

        val reaperBody = ReaperBody(mainBody, camera)
        val reaperSprite = ReaperSprite(initialSprite)
        eventManager.addListener(reaperBody, StatePressedEvent::class)
        eventManager.addListener(reaperBody, StateReleasedEvent::class)
        eventManager.addListener(reaperSprite, StateReleasedEvent::class)
        eventManager.addListener(reaperSprite, StatePressedEvent::class)

        return ReaperEntity(reaperBody, reaperSprite)
    }
}