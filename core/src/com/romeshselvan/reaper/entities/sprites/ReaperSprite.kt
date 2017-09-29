package com.romeshselvan.reaper.entities.sprites

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.romeshselvan.reaper.assets.ReaperTextures
import com.romeshselvan.reaper.defaults.GameStates
import com.romeshselvan.reaper.engine.entities.SpriteObject
import com.romeshselvan.reaper.engine.input.data.State
import com.romeshselvan.reaper.engine.input.listeners.InputStateListener

class ReaperSprite(sprite: Sprite): SpriteObject(sprite), InputStateListener {

    private var animateFrameTime = 0.2f

    private var downAnimate = false
    private var upAnimate = false
    private var leftAnimate = false
    private var rightAnimate = false

    private val downAnimation = Animation<TextureRegion>(0.25f, ReaperTextures.downFacingSet)
    private val upAnimation = Animation<TextureRegion>(0.25f, ReaperTextures.upFacingSet)
    private val rightAnimation = Animation<TextureRegion>(0.25f, ReaperTextures.rightFacingSet)
    private val leftAnimation = Animation<TextureRegion>(0.25f, ReaperTextures.leftFacingSet)

    override fun render(delta: Float, batch: Batch, xPos: Float, yPos: Float) {
        batch.begin()
        if(downAnimate || upAnimate || rightAnimate || leftAnimate) {
            animateFrameTime.plus(delta)
        }

        if(downAnimate) sprite.setRegion(downAnimation.getKeyFrame(animateFrameTime, downAnimate))
        if(leftAnimate) sprite.setRegion(leftAnimation.getKeyFrame(animateFrameTime, leftAnimate))
        if(rightAnimate) sprite.setRegion(rightAnimation.getKeyFrame(animateFrameTime, rightAnimate))
        if(upAnimate) sprite.setRegion(upAnimation.getKeyFrame(animateFrameTime, upAnimate))

        sprite.setPosition(xPos, yPos)
        sprite.draw(batch)
        batch.end()
    }

    override fun onStatePressed(state: State) {
        when(state.stateId) {
            GameStates.MOVE_UP.name -> upAnimate = true
            GameStates.MOVE_DOWN.name -> downAnimate = true
            GameStates.MOVE_RIGHT.name -> rightAnimate = true
            GameStates.MOVE_LEFT.name -> leftAnimate = true
        }
    }

    override fun onStateReleased(state: State) {
        when(state.stateId) {
            GameStates.MOVE_UP.name -> {
                sprite.setRegion(ReaperTextures.upFacingSet[0])
                animateFrameTime = 0.2f
                upAnimate = false
            }
            GameStates.MOVE_DOWN.name -> {
                sprite.setRegion(ReaperTextures.downFacingSet[0])
                animateFrameTime = 0.2f
                downAnimate = false
            }
            GameStates.MOVE_RIGHT.name -> {
                sprite.setRegion(ReaperTextures.rightFacingSet[0])
                animateFrameTime = 0.2f
                rightAnimate = false
            }
            GameStates.MOVE_LEFT.name -> {
                sprite.setRegion(ReaperTextures.leftFacingSet[0])
                animateFrameTime = 0.2f
                leftAnimate = false
            }
        }
    }

}