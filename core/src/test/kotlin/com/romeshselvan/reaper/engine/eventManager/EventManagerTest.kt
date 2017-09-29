package com.romeshselvan.reaper.engine.eventManager

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

interface EventListenerTest {
    fun onListen(value: String)
}

class TestEvent : Event<EventListenerTest> {
    override fun notify(listener: EventListenerTest) {
        listener.onListen("Test Success")
    }
}

class EventListenerOne(var result: String = "nothing") : EventListenerTest {
    override fun onListen(value: String) {
        result = value
    }
}

class EventListenerTwo(var result: String = "nothing") : EventListenerTest {
    override fun onListen(value: String) {
        result = value
    }
}

class EventManagerTest {

    @Before
    fun setUp() = EventManagerImpl.clearListenerMap()

    @Test
    fun `Manager should add event handler when no other handler is available`() {
        val eventHandler = EventListenerOne()
        EventManagerImpl.getListenerMap() shouldBe emptyMap<Any, Any>()

        EventManagerImpl.addListener(eventHandler, TestEvent::class)
        EventManagerImpl.getListenerMap() shouldNotBe emptyMap<Any, Any>()

        EventManagerImpl.getListenerMap()[TestEvent::class]?.contains(eventHandler) shouldBe true
    }

    @Test
    fun `Manager should add an event handler to an existing list if the Listener is found`(){
        val eventHandler = EventListenerOne()
        val eventHandlerTwo = EventListenerTwo()

        EventManagerImpl.addListener(eventHandler, TestEvent::class)
        EventManagerImpl.getListenerMap() shouldNotBe emptyMap<Any, Any>()

        EventManagerImpl.addListener(eventHandlerTwo, TestEvent::class)
        EventManagerImpl.getListenerMap().size shouldBe 1

        EventManagerImpl.getListenerMap()[TestEvent::class]?.containsAll(listOf(eventHandler, eventHandlerTwo)) shouldBe true
    }

    @Test
    fun `Manager should send the event to the event listener if the correct event is sent`() {
        val eventListenerOne = EventListenerOne()
        EventManagerImpl.addListener(eventListenerOne, TestEvent::class)
        eventListenerOne.result shouldBe "nothing"

        EventManagerImpl.notifyListeners(TestEvent())
        eventListenerOne.result shouldBe "Test Success"
    }

    @Test
    fun `Manager should throw an exception when trying to send a Notification for a game event which does not exist`() {
        val eventListenerOne = EventListenerOne()
        eventListenerOne.result shouldBe "nothing"

        assertFailsWith<IllegalArgumentException> {
            EventManagerImpl.notifyListeners(TestEvent())
        }
    }

    @Test
    fun `Manager should remove a listener from the list when it is found`() {
        val eventListenerOne = EventListenerOne()
        val eventListenerTwo = EventListenerTwo()
        val eventListenerThree = EventListenerOne("three")
        EventManagerImpl.addListener(eventListenerOne, TestEvent::class)
        EventManagerImpl.addListener(eventListenerTwo, TestEvent::class)
        EventManagerImpl.addListener(eventListenerThree, TestEvent::class)

        EventManagerImpl.getListenerMap().size shouldBe 1

        EventManagerImpl.getListenerMap()[TestEvent::class]?.containsAll(listOf(eventListenerOne, eventListenerTwo, eventListenerThree)) shouldBe true

        EventManagerImpl.removeListener(eventListenerTwo, TestEvent::class)
        EventManagerImpl.getListenerMap()[TestEvent::class]?.containsAll(listOf(eventListenerOne, eventListenerThree)) shouldBe true
    }

    @Test
    fun `Manager should do nothing if the list for the clazz is empty when trying to remove a listener`() {
        val eventListenerOne = EventListenerOne()
        val eventListenerTwo = EventListenerTwo()

        EventManagerImpl.addListener(eventListenerOne, TestEvent::class)
        EventManagerImpl.getListenerMap().size shouldBe 1
        val list = EventManagerImpl.getListenerMap()[TestEvent::class]
        list?.contains(eventListenerOne) shouldBe true

        EventManagerImpl.removeListener(eventListenerOne, TestEvent::class)
        EventManagerImpl.getListenerMap()[TestEvent::class]?.isEmpty() shouldBe true

        EventManagerImpl.removeListener(eventListenerTwo, TestEvent::class)
        EventManagerImpl.getListenerMap()[TestEvent::class]?.isEmpty() shouldBe true
    }

    @Test
    fun `Manager should not throw an exception and do nothing if the list clazz does not exist in the map when trying to remove a listener`() {
        EventManagerImpl.getListenerMap().isEmpty() shouldBe true
        EventManagerImpl.removeListener(EventListenerTwo(), TestEvent::class)
    }
}