package com.romeshselvan.reaper.engine.eventManager

import kotlin.reflect.KClass

interface EventManager {
    fun addListener(listener : Any, clazz: KClass<out Event<*>>)
    fun removeListener(listener: Any, clazz: KClass<out Event<*>>)
    fun <T> notifyListeners(event: Event<T>)
    fun clearListenerMap()

    companion object {
        fun getEventManager(): EventManager = EventManagerImpl
    }
}

private object EventManagerImpl: EventManager {
    private val listenerMap: MutableMap<KClass<out Event<*>>, MutableList<Any>> = mutableMapOf()

    override fun addListener(listener : Any, clazz: KClass<out Event<*>>) {
        val list = listenerMap[clazz] ?: mutableListOf()
        list.add(listener)
        listenerMap.put(clazz, list)
    }

    override fun removeListener(listener: Any, clazz: KClass<out Event<*>>) {
        val mutableList = listenerMap[clazz]
        mutableList?.remove(listener)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> notifyListeners(event: Event<T>) {
        val list = listenerMap[event::class] ?: throw IllegalArgumentException("Event Type not found")
        val newList = list as MutableList<T>
        newList.forEach { event.notify(it) }
    }

    override fun clearListenerMap() = listenerMap.clear()

    fun getListenerMap(): Map<KClass<out Event<*>>, List<Any>> = listenerMap.toMap()
}

