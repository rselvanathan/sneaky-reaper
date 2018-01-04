package com.romeshselvan.reaper

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.engine.entityBuilders.EntityBodyBuilderUtil
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.entities.KnightEntity
import com.romeshselvan.reaper.entities.ReaperEntity
import com.romeshselvan.reaper.entities.WallEntity
import com.romeshselvan.reaper.entities.builders.KnightBuilder
import com.romeshselvan.reaper.entities.builders.ReaperBuilder
import com.romeshselvan.reaper.entities.builders.WallBuilder

class GameObjectProducer(private val builderUtil: EntityBodyBuilderUtil,
                                     private val wallBuilder: WallBuilder,
                                     private val reaperBuilder: ReaperBuilder) {

    fun generateReaper(tiledMap: TiledMap): ReaperEntity =
            reaperBuilder.build(tiledMap.layers["ObjectPositions"].objects["start"]) as ReaperEntity

    fun generateKnights(world: World, rayHandler: RayHandler, tiledMap: TiledMap, targetEntity: EntityBody)
            : List<KnightEntity> {
        // First Knight value in tiled is set to 1
        var iterator = 1
        return tiledMap.layers["ObjectPositions"]
                .objects.filter { it.properties["type", String::class.java] == "knight" }
                .map { KnightBuilder(world, builderUtil, rayHandler, iterator++, targetEntity, tiledMap).build(it) as KnightEntity }
    }

    fun generateWalls(tiledMap: TiledMap): List<WallEntity> =
         tiledMap.layers["CollisionObjects"]
                 .objects
                 .filter { it.properties["type", String::class.java] == "wall" }
                 .map { wallBuilder.build(it) as WallEntity }

    fun generateWallLights(rayHandler: RayHandler, tiledMap: TiledMap) {
         tiledMap.layers["ObjectPositions"]
                 .objects.filter { it.properties["type", String::class.java] == "wallLight" }
                 .map { generateWallLightFromObject(it, rayHandler) }
    }

    private fun generateWallLightFromObject(mapObject: MapObject, rayHandler: RayHandler) {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        val pointLight = PointLight(rayHandler, 200, Color.YELLOW, 350.0f, x, y)
        pointLight.setContactFilter(builderUtil.getPointLightFilter())
        pointLight.setSoftnessLength(100.0f)
    }

    companion object {
        fun getGameObjectProducer(world: World, camera: OrthographicCamera, eventManager: EventManager): GameObjectProducer {
            val builderUtil = EntityBodyBuilderUtil()
            return GameObjectProducer(
                    builderUtil,
                    WallBuilder(world, builderUtil),
                    ReaperBuilder(world, builderUtil, camera, eventManager))
        }
    }
}
