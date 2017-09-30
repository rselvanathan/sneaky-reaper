package com.romeshselvan.reaper.engine.input

import com.romeshselvan.reaper.engine.input.data.Action
import com.romeshselvan.reaper.engine.input.data.InputContext
import com.romeshselvan.reaper.engine.input.data.State
import io.kotlintest.matchers.shouldBe
import org.junit.Test

class InputContextLoaderImplTest {
    private val inputContextLoader = InputContextLoader.getInputContextLoader();

    @Test
    fun `Loader should load the json object passed in correctly producing a list of contexts`() {
        val menuContext = InputContext("menu", mapOf(19 to Action("MENU_UP")), emptyMap())
        val gameContext = InputContext("game",
                mapOf(131 to Action("PAUSE")),
                mapOf(19 to State("MOVE_UP"), 20 to State("MOVE_DOWN")))
        val expectedContextList = listOf(menuContext, gameContext)
        val resultContextList = inputContextLoader.loadInputContexts(jsonToRead())
        resultContextList.containsAll(expectedContextList) shouldBe true
    }

    private fun jsonToRead() =
            """
            {
              "contexts" :
              [
                {
                  "contextName" : "menu",
                  "actions" : [
                    {
                      "key" : 19,
                      "action" : "MENU_UP"
                    }
                  ],
                  "states" : []
                },
                {
                  "contextName" : "game",
                  "actions" : [
                    {
                      "key" : 131,
                      "action" : "PAUSE"
                    }
                  ],
                  "states" : [
                    {
                      "key" : 19,
                      "state" : "MOVE_UP"
                    },
                    {
                      "key" : 20,
                      "state" : "MOVE_DOWN"
                    }
                  ]
                }
              ]
            }
            """.trimIndent()
}