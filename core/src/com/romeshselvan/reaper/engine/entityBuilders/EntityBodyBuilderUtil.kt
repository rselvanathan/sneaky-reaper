package com.romeshselvan.reaper.engine.entityBuilders

import com.badlogic.gdx.physics.box2d.*

/**
 * A class that contains the raw basic functions to build an EntityBody
 *
 * @author Romesh Selvanathan
 */
class EntityBodyBuilderUtil {

    /**
     * The Filter category value for a Point Light
     */
    private val POINT_LIGHT_FILTER_CATEGORY: Short = 0x0016
    /**
     * The Filter category value for any sensor type body
     */
    private val SENSOR_FILTER_CATEGORY: Short = 0x0001

    /**
     * Build and return a dynamic Body, which will apply forces and accept forces (will move and move other objects)
     *
     * @param xPos x position of the object
     * @param yPos y position of the object
     * @return a BodyDef object to build a physics body
     */
    fun getDynamicBody(xPos: Float, yPos: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(xPos, yPos)
        bodyDef.fixedRotation = true
        return bodyDef
    }

    /**
     * Build and return a static Body, which will only apply forces (will not move but make other objects move)
     *
     * @param xPos x position of the object
     * @param yPos y position of the object
     * @return a BodyDef object to build a physics body
     */
    fun getStaticBody(xPos: Float, yPos: Float): BodyDef {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.position.set(xPos, yPos)
        return bodyDef
    }

    /**
     * Get a circular sensor object, to be used for collision detection e.g Range checks
     *
     * @param circleShape The shape of the Fixture
     * @return retrieve a fixture definition
     */
    fun getCircularSensor(circleShape: CircleShape) : FixtureDef {
        val bodyCheckFixtureDef = FixtureDef()
        bodyCheckFixtureDef.shape = circleShape
        bodyCheckFixtureDef.isSensor = true
        return bodyCheckFixtureDef
    }

    /**
     * Return a Box shape
     *
     * @param width width of the box
     * @param height height of the box
     * @return The Rectangular shape
     */
    fun getBoxPolygonShape(width: Float, height: Float): PolygonShape {
        val polygonShape = PolygonShape()
        polygonShape.setAsBox(width, height)
        return polygonShape
    }

    /**
     * Return a Circle Shape
     *
     * @param xPos x position of the shape
     * @param yPos y position of the shape
     * @param radius radius of the circle
     * @return The Circle shape
     */
    fun getCirclePolygonShape(xPos: Float, yPos: Float, radius: Float): CircleShape {
        val circleShape = CircleShape()
        circleShape.position.set(xPos, yPos)
        circleShape.radius = radius
        return circleShape
    }

    /**
     * The Default Fixture Definition object
     *
     * @param polygonShape A shape that will represent the fixture
     * @return A Fixture Definition
     */
    fun getDefaultFixtureDef(polygonShape: PolygonShape): FixtureDef {
        val fixtureDef = FixtureDef()
        fixtureDef.shape = polygonShape
        fixtureDef.density = 1.0f
        fixtureDef.restitution = 0.0f
        return fixtureDef
    }

    /**
     * Get a Collision Filter for a Sensor, that will collide with everything
     */
    fun getSensorFilter() : Filter {
        val filter = Filter()
        filter.categoryBits = SENSOR_FILTER_CATEGORY
        // Collide with everything
        filter.maskBits = 1
        return filter
    }

    /**
     * Get a Collision filter for a Point Light, that will ignore Sensors
     */
    fun getPointLightFilter() : Filter {
        val filter = Filter()
        filter.categoryBits = POINT_LIGHT_FILTER_CATEGORY
        filter.maskBits = SENSOR_FILTER_CATEGORY
        return filter
    }
}