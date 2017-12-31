package com.romeshselvan.reaper.assets

import com.badlogic.gdx.graphics.Texture

/**
 * Will load and manage the raw assets from the "assets" folder
 *
 * @author Romesh Selvanathan
 */
object AssetManager {

    val reaperSheet = Texture("death_scythe.png")
    val knightSheet = Texture("darkknight_v1.png")

    fun dispose() {
        reaperSheet.dispose()
        knightSheet.dispose()
    }
}