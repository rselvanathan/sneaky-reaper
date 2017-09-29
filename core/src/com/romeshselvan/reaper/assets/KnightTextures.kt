package com.romeshselvan.reaper.assets

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

object KnightTextures {
    private val width = 48
    private val height = 72

    val downFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.knightSheet, width*it, 0, width, height) }.toTypedArray())
    val upFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.knightSheet, width*it, height*3, width, height) }.toTypedArray())
    val rightFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.knightSheet, width*it, height*2, width, height) }.toTypedArray())
    val leftFacingSet: Array<TextureRegion> =
            Array((0 until 3).map { TextureRegion(AssetManager.knightSheet, width*it, height, width, height) }.toTypedArray())

    fun dispose() {
        downFacingSet.forEach { it.texture.dispose() }
        upFacingSet.forEach { it.texture.dispose() }
        rightFacingSet.forEach { it.texture.dispose() }
        leftFacingSet.forEach { it.texture.dispose() }
    }
}