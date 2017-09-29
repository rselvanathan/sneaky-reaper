package com.romeshselvan.reaper

import box2dLight.ConeLight
import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.assets.KnightTextures
import com.romeshselvan.reaper.assets.ReaperTextures
import com.romeshselvan.reaper.engine.entities.EntityBody
import com.romeshselvan.reaper.engine.eventManager.EventManager
import com.romeshselvan.reaper.engine.input.events.StatePressedEvent
import com.romeshselvan.reaper.engine.input.events.StateReleasedEvent
import com.romeshselvan.reaper.entities.KnightEntity
import com.romeshselvan.reaper.entities.ReaperEntity
import com.romeshselvan.reaper.entities.WallEntity
import com.romeshselvan.reaper.entities.bodies.KnightBody
import com.romeshselvan.reaper.entities.bodies.ReaperBody
import com.romeshselvan.reaper.entities.bodies.WallBody
import com.romeshselvan.reaper.entities.sprites.KnightSprite
import com.romeshselvan.reaper.entities.sprites.ReaperSprite

interface GameObjectProducer {
    fun generateReaper(world: World, tiledMap: TiledMap, camera: OrthographicCamera, eventManager: EventManager): ReaperEntity
    fun generateKnights(world: World, rayHandler: RayHandler, tiledMap: TiledMap, targetEntity: EntityBody): List<KnightEntity>
    fun generateWalls(world: World, tiledMap: TiledMap): List<WallEntity>

    fun generateWallLights(rayHandler: RayHandler, tiledMap: TiledMap)

    companion object {
        fun getGameObjectProducer(): GameObjectProducer = GameObjectProducerImpl
    }
}

private object GameObjectProducerImpl : GameObjectProducer {

    override fun generateReaper(world: World, tiledMap: TiledMap, camera: OrthographicCamera, eventManager: EventManager): ReaperEntity {
        val reaperStartPosition: MapObject = tiledMap.layers["ObjectPositions"].objects["start"]
        val x = reaperStartPosition.properties["x", Float::class.java]
        val y = reaperStartPosition.properties["y", Float::class.java]
        return makeReaper(world, x, y, camera, eventManager)
    }

    override fun generateKnights(world: World, rayHandler: RayHandler, tiledMap: TiledMap, targetEntity: EntityBody): List<KnightEntity> {
        return tiledMap.layers["ObjectPositions"]
                .objects.filter { it.properties["type", String::class.java] == "knight" }
                .map { generateKnightFromMapObject(world, rayHandler, targetEntity, it) }
    }

    override fun generateWalls(world: World, tiledMap: TiledMap): List<WallEntity> {
        return tiledMap.layers["CollisionObjects"]
                .objects
                .filter { it.properties["type", String::class.java] == "wall" }
                .map { generateWallFromMapObject(world, it) }
    }

    override fun generateWallLights(rayHandler: RayHandler, tiledMap: TiledMap) {
         tiledMap.layers["ObjectPositions"]
                 .objects.filter { it.properties["type", String::class.java] == "wallLight" }
                 .map { generateWallLightFromObject(it, rayHandler) }
    }

    private fun generateWallLightFromObject(mapObject: MapObject, rayHandler: RayHandler) {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        val pointLight = PointLight(rayHandler, 200, Color.YELLOW, 350.0f, x, y)
        pointLight.setSoftnessLength(100.0f)
    }

    private fun generateWallFromMapObject(world: World, mapObject: MapObject): WallEntity {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        val width = mapObject.properties["width", Float::class.java]/2
        val height = mapObject.properties["height", Float::class.java]/2
        return makeWall(world, x+width, y+height, width, height)
    }

    private fun generateKnightFromMapObject(world: World, rayHandler: RayHandler, targetEntity: EntityBody, mapObject: MapObject): KnightEntity {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        return makeKnight(world, rayHandler, x, y, targetEntity)
    }

    private fun makeReaper(world: World, xPos: Float, yPos: Float, camera: OrthographicCamera, eventManager: EventManager): ReaperEntity {
        val mainBody = world.createBody(getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(ReaperTextures.upFacingSet[0])
        val boxPolygonShape = getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val fixtureDef = getDefaultFixtureDef(boxPolygonShape)

        mainBody.createFixture(fixtureDef)
        boxPolygonShape.dispose()

        val reaperBody = ReaperBody(mainBody, camera)
        val reaperSprite = ReaperSprite(initialSprite)
        eventManager.addListener(reaperBody, StatePressedEvent::class)
        eventManager.addListener(reaperBody, StateReleasedEvent::class)
        eventManager.addListener(reaperSprite, StateReleasedEvent::class)
        eventManager.addListener(reaperSprite, StatePressedEvent::class)

        return ReaperEntity(reaperBody, reaperSprite)
    }

    private fun makeWall(world: World, xPos: Float, yPos: Float, width: Float, height: Float): WallEntity {
        val staticBody = getStaticBody(xPos, yPos, width, height)
        val mainBody = world.createBody(staticBody)
        val boxPolygonShape = getBoxPolygonShape(width, height)
        val defaultFixtureDef = getDefaultFixtureDef(boxPolygonShape)
        mainBody.createFixture(defaultFixtureDef)
        boxPolygonShape.dispose()
        return WallEntity(WallBody(mainBody))
    }

    private fun makeKnight(world: World, rayHandler: RayHandler, xPos: Float, yPos: Float, targetEntity: EntityBody): KnightEntity {
        val mainBody = world.createBody(getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(KnightTextures.upFacingSet[0])
        val boxPolygonShape = getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val defaultFixtureDef = getDefaultFixtureDef(boxPolygonShape)
        val coneLight = ConeLight(rayHandler, 100, Color.RED, 250.0f, xPos, yPos, 90.0f, 35.0f)

        mainBody.createFixture(defaultFixtureDef)
        boxPolygonShape.dispose()

        return KnightEntity(KnightBody(mainBody, targetEntity, coneLight), KnightSprite(initialSprite))
    }

    private fun getDynamicBody(xPos: Float, yPos: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(xPos, yPos)
        bodyDef.fixedRotation = true
        return bodyDef
    }

    private fun getStaticBody(xPos: Float, yPos: Float, width: Float, height: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(xPos, yPos)
        return bodyDef
    }

    private fun getBoxPolygonShape(width: Float, height: Float): PolygonShape {
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(width, height)
        return polygonShape
    }

    private fun getDefaultFixtureDef(polygonShape: PolygonShape): FixtureDef {
        val fixtureDef = FixtureDef()
        fixtureDef.shape = polygonShape
        fixtureDef.density = 1.0f
        fixtureDef.restitution = 0.0f
        return fixtureDef;
    }
}