package com.romeshselvan.reaper.defaults

/**
 * This represents the input context the application contains. E.g when the Game screen is active the appropriate
 * context will be loaded with a different input mapping to when the application is in the menu screen.
 *
 * @author Romesh Selvanathan
 */
enum class InputContextType(val contextName: String) {
    GAME_CONTEXT("game"),
    MENU_CONTEXT("menu")
}