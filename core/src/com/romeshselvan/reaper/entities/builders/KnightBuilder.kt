package com.romeshselvan.reaper.entities.builders

import box2dLight.ConeLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.assets.KnightTextures
import com.romeshselvan.reaper.engine.collision.FixtureType
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.engine.entityBuilders.EntityBodyBuilderUtil
import com.romeshselvan.reaper.engine.entityBuilders.EntityBuilder
import com.romeshselvan.reaper.entities.KnightEntity
import com.romeshselvan.reaper.entities.bodies.KnightBody
import com.romeshselvan.reaper.entities.sprites.KnightSprite

class KnightBuilder(private val world: World,
                    private val builderUtil: EntityBodyBuilderUtil,
                    private val rayHandler: RayHandler,
                    private val knightId : Int,
                    private val targetEntity: EntityBody,
                    private val tiledMap: TiledMap) : EntityBuilder {

    override fun build(mapObject: MapObject): Entity {
        val patrolDestinations = getPatrolDestinations(knightId, tiledMap)
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        return makeKnight(world, rayHandler, x, y, targetEntity, patrolDestinations)
    }

    private fun getPatrolDestinations(knightId: Int, tiledMap: TiledMap) : List<Vector2> {
        val mapObjectList =
                tiledMap.layers["ObjectPositions"]
                        .objects.filter { it.properties["type", String::class.java] == "knightPatrolPosition" &&
                                          it.properties["knightNumber", Int::class.java] == knightId }
                        .toCollection(mutableListOf())
        mapObjectList.sortBy { it.properties["positionNumber", Int::class.java] }
        return mapObjectList.map { Vector2(it.properties["x", Float::class.java],
                it.properties["y", Float::class.java]) }
    }

    private fun makeKnight(world: World,
                           rayHandler: RayHandler,
                           xPos: Float,
                           yPos: Float,
                           targetEntity: EntityBody,
                           patrolDestinations: List<Vector2>): KnightEntity {
        val mainBody = world.createBody(builderUtil.getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(KnightTextures.upFacingSet[0])
        val boxPolygonShape = builderUtil.getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val defaultFixtureDef = builderUtil.getDefaultFixtureDef(boxPolygonShape)
        val coneLight = ConeLight(rayHandler, 100, Color.RED, 250.0f, xPos, yPos, 90.0f, 35.0f)

        mainBody.createFixture(defaultFixtureDef).userData = FixtureType.DEFAULT_BODY
        boxPolygonShape.dispose()

        // Generate Sensor Fixtures
        val outerRangeCheckShape = builderUtil.getCirclePolygonShape(xPos, yPos, 200.0f)
        val midRangeCheckShape = builderUtil.getCirclePolygonShape(xPos, yPos, 130.0f)
        val closeRangeCheckShape = builderUtil.getCirclePolygonShape(xPos, yPos, 80.0f)

        val outerCheckFixture = mainBody.createFixture(builderUtil.getCircularSensor(outerRangeCheckShape))
        outerCheckFixture.userData = FixtureType.AI_OUTER_CHASE_CHECK
        outerCheckFixture.filterData = builderUtil.getSensorFilter()

        val midCheckFixture = mainBody.createFixture(builderUtil.getCircularSensor(midRangeCheckShape))
        midCheckFixture.userData = FixtureType.AI_CHASE_CHECK
        midCheckFixture.filterData = builderUtil.getSensorFilter()

        val closeCheckFixture = mainBody.createFixture(builderUtil.getCircularSensor(closeRangeCheckShape))
        closeCheckFixture.userData = FixtureType.AI_CHASE_CHECK
        closeCheckFixture.filterData = builderUtil.getSensorFilter()

        outerRangeCheckShape.dispose()
        midRangeCheckShape.dispose()
        closeRangeCheckShape.dispose()

        return KnightEntity(KnightBody(mainBody, targetEntity, patrolDestinations, world, coneLight),
                KnightSprite(initialSprite))
    }
}