package com.romeshselvan.reaper

import box2dLight.RayHandler
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.World
import com.romeshselvan.reaper.engine.entities.Entity
import com.romeshselvan.reaper.engine.eventManager.EventManager

class GameWorld(gameObjectProducer: GameObjectProducer,
                collisionListener: ContactListener,
                eventManager: EventManager,
                private val camera: OrthographicCamera,
                private var entityList: MutableList<Entity>,
                private val world: World,
                private val rayHandler: RayHandler,
                private val tileMap: TiledMap,
                private val tileMapRenderer: TiledMapRenderer) {

    private val debugRenderer: Box2DDebugRenderer = Box2DDebugRenderer()

    init {
        world.setContactListener(collisionListener)
        val reaper = gameObjectProducer.generateReaper(world, tileMap, camera, eventManager)
        entityList.add(reaper)
        entityList.addAll(gameObjectProducer.generateWalls(world, tileMap))
        entityList.addAll(gameObjectProducer.generateKnights(world, rayHandler, tileMap, reaper.body))
        gameObjectProducer.generateWallLights(rayHandler, tileMap)
        rayHandler.setAmbientLight(0.0f, 0.0f, 0.0f, 0.4f)
    }

    fun update(delta: Float) {
        world.step(1.0f/60f, 6, 2)
        entityList.forEach { it.update(delta) }
        camera.update()
    }

    fun render(delta: Float, batch: Batch) {
        tileMapRenderer.setView(camera)
        tileMapRenderer.render()

        batch.projectionMatrix = camera.combined

        entityList.forEach { it.render(delta, batch) }

        rayHandler.setCombinedMatrix(camera)
        rayHandler.updateAndRender()
        debugRenderer.render(world, camera.combined)
    }

    fun dispose() {
        entityList.forEach { it.dispose() }
        tileMap.dispose()
    }

    companion object {
        fun getInstance(gameObjectProducer: GameObjectProducer,
                        collisionListener: ContactListener,
                        eventManager: EventManager,
                        camera: OrthographicCamera): GameWorld {
            val world = World(Vector2(0.0f, 0.0f), false)
            val tiledMap = TmxMapLoader().load("level1.tmx")
            val tiledMapRenderer = OrthoCachedTiledMapRenderer(tiledMap)
            val rayHandler = RayHandler(world)
            return GameWorld(gameObjectProducer, collisionListener, eventManager, camera, mutableListOf(), world,
                    rayHandler, tiledMap, tiledMapRenderer)
        }
    }
}