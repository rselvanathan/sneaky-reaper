package com.romeshselvan.reaper

import box2dLight.ConeLight
import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.romeshselvan.reaper.assets.KnightTextures
import com.romeshselvan.reaper.assets.ReaperTextures
import com.romeshselvan.reaper.engine.collision.FixtureType
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

    private val POINT_LIGHT_FILTER_CATEGORY: Short = 0x0016
    private val SENSOR_FILTER_CATEGORY: Short = 0x0001

    override fun generateReaper(world: World, tiledMap: TiledMap, camera: OrthographicCamera,
                                eventManager: EventManager): ReaperEntity {
        val reaperStartPosition: MapObject = tiledMap.layers["ObjectPositions"].objects["start"]
        val x = reaperStartPosition.properties["x", Float::class.java]
        val y = reaperStartPosition.properties["y", Float::class.java]
        return makeReaper(world, x, y, camera, eventManager)
    }

    override fun generateKnights(world: World, rayHandler: RayHandler, tiledMap: TiledMap,
                                 targetEntity: EntityBody): List<KnightEntity> {
        // First Knight value in tiled is set to 1
        var iterator = 1
        return tiledMap.layers["ObjectPositions"]
                .objects.filter { it.properties["type", String::class.java] == "knight" }
                .map { generateKnightFromMapObject(world, rayHandler, targetEntity, it, iterator++, tiledMap) }
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
        pointLight.setContactFilter(getPointLightFilter())
        pointLight.setSoftnessLength(100.0f)
    }

    private fun generateWallFromMapObject(world: World, mapObject: MapObject): WallEntity {
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        val width = mapObject.properties["width", Float::class.java]/2
        val height = mapObject.properties["height", Float::class.java]/2
        return makeWall(world, x+width, y+height, width, height)
    }

    private fun generateKnightFromMapObject(world: World, rayHandler: RayHandler, targetEntity: EntityBody,
                                            mapObject: MapObject, knightIterator: Int, tiledMap: TiledMap): KnightEntity {
        val patrolDestinations = getPatrolDestinations(knightIterator, tiledMap)
        val x = mapObject.properties["x", Float::class.java]
        val y = mapObject.properties["y", Float::class.java]
        return makeKnight(world, rayHandler, x, y, targetEntity, patrolDestinations)
    }

    private fun getPatrolDestinations(knightIterator: Int, tiledMap: TiledMap) : List<Vector2> {
        val mapObjectList = tiledMap.layers["ObjectPositions"]
                .objects.filter { it.properties["type", String::class.java] == "knightPatrolPosition" &&
                                  it.properties["knightNumber", Int::class.java] == knightIterator }
                .toCollection(mutableListOf())
        mapObjectList.sortBy { it.properties["positionNumber", Int::class.java] }
        return mapObjectList.map { Vector2(it.properties["x", Float::class.java],
                                           it.properties["y", Float::class.java]) }
    }

    private fun makeReaper(world: World, xPos: Float, yPos: Float, camera: OrthographicCamera, eventManager: EventManager): ReaperEntity {
        val mainBody = world.createBody(getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(ReaperTextures.upFacingSet[0])
        val boxPolygonShape = getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val fixtureDef = getDefaultFixtureDef(boxPolygonShape)

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

    private fun makeWall(world: World, xPos: Float, yPos: Float, width: Float, height: Float): WallEntity {
        val staticBody = getStaticBody(xPos, yPos)
        val mainBody = world.createBody(staticBody)
        val boxPolygonShape = getBoxPolygonShape(width, height)
        val defaultFixtureDef = getDefaultFixtureDef(boxPolygonShape)
        mainBody.createFixture(defaultFixtureDef).userData = FixtureType.DEFAULT_BODY
        boxPolygonShape.dispose()
        return WallEntity(WallBody(mainBody))
    }

    private fun makeKnight(world: World, rayHandler: RayHandler, xPos: Float, yPos: Float, targetEntity: EntityBody, patrolDestinations: List<Vector2>): KnightEntity {
        val mainBody = world.createBody(getDynamicBody(xPos, yPos))
        val initialSprite = Sprite(KnightTextures.upFacingSet[0])
        val boxPolygonShape = getBoxPolygonShape(initialSprite.width / 3, initialSprite.height / 2)
        val defaultFixtureDef = getDefaultFixtureDef(boxPolygonShape)
        val coneLight = ConeLight(rayHandler, 100, Color.RED, 250.0f, xPos, yPos, 90.0f, 35.0f)

        mainBody.createFixture(defaultFixtureDef).userData = FixtureType.DEFAULT_BODY
        boxPolygonShape.dispose()

        // Generate Sensor Fixtures
        val outerRangeCheckShape = getCirclePolygonShape(xPos, yPos, 200.0f)
        val midRangeCheckShape = getCirclePolygonShape(xPos, yPos, 130.0f)
        val closeRangeCheckShape = getCirclePolygonShape(xPos, yPos, 80.0f)

        val outerCheckFixture = mainBody.createFixture(getCircularSensor(outerRangeCheckShape))
        outerCheckFixture.userData = FixtureType.AI_OUTER_CHASE_CHECK
        outerCheckFixture.filterData = getSensorFilter()

        val midCheckFixture = mainBody.createFixture(getCircularSensor(midRangeCheckShape))
        midCheckFixture.userData = FixtureType.AI_CHASE_CHECK
        midCheckFixture.filterData = getSensorFilter()

        val closeCheckFixture = mainBody.createFixture(getCircularSensor(closeRangeCheckShape))
        closeCheckFixture.userData = FixtureType.AI_CHASE_CHECK
        closeCheckFixture.filterData = getSensorFilter()

        outerRangeCheckShape.dispose()
        midRangeCheckShape.dispose()
        closeRangeCheckShape.dispose()

        return KnightEntity(KnightBody(mainBody, targetEntity, patrolDestinations, world, coneLight),
                            KnightSprite(initialSprite))
    }

    private fun getDynamicBody(xPos: Float, yPos: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(xPos, yPos)
        bodyDef.fixedRotation = true
        return bodyDef
    }

    private fun getStaticBody(xPos: Float, yPos: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(xPos, yPos)
        return bodyDef
    }

    private fun getCircularSensor(circleShape: CircleShape) : FixtureDef {
        val bodyCheckFixtureDef = FixtureDef()
        bodyCheckFixtureDef.shape = circleShape
        bodyCheckFixtureDef.isSensor = true
        return bodyCheckFixtureDef
    }

    private fun getBoxPolygonShape(width: Float, height: Float): PolygonShape {
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(width, height)
        return polygonShape
    }

    private fun getCirclePolygonShape(xPos: Float, yPos: Float, radius: Float): CircleShape {
        val circleShape = CircleShape()
        circleShape.position.set(xPos, yPos)
        circleShape.radius = radius
        return circleShape
    }

    private fun getDefaultFixtureDef(polygonShape: PolygonShape): FixtureDef {
        val fixtureDef = FixtureDef()
        fixtureDef.shape = polygonShape
        fixtureDef.density = 1.0f
        fixtureDef.restitution = 0.0f
        return fixtureDef
    }

    private fun getSensorFilter() : Filter {
        val filter = Filter()
        filter.categoryBits = SENSOR_FILTER_CATEGORY
        // Collide with everything
        filter.maskBits = 1
        return filter
    }

    private fun getPointLightFilter() : Filter {
        val filter = Filter()
        filter.categoryBits = POINT_LIGHT_FILTER_CATEGORY
        filter.maskBits = SENSOR_FILTER_CATEGORY
        return filter
    }
}
