package com.romeshselvan.reaper.assets

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

object ReaperTextures {
    private val dimension = 64

    val downFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.reaperSheet, dimension*it, 0, dimension, dimension) }.toTypedArray())
    val upFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.reaperSheet, dimension*it, dimension*3, dimension, dimension) }.toTypedArray())
    val rightFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.reaperSheet, dimension*it, dimension*2, dimension, dimension) }.toTypedArray())
    val leftFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.reaperSheet, dimension*it, dimension, dimension, dimension) }.toTypedArray())

    fun dispose() {
        downFacingSet.forEach { it.texture.dispose() }
        upFacingSet.forEach { it.texture.dispose() }
        rightFacingSet.forEach { it.texture.dispose() }
        leftFacingSet.forEach { it.texture.dispose() }
    }
}