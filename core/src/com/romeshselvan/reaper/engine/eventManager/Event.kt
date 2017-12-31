package com.romeshselvan.reaper.engine.eventManager

/**
 * Represents an Event that will notify it's assigned listener. The implementing Event object can contain any custom
 * data that the listener will require. The listener will typically be an interface, with some predefined method which
 * will expect a specific set of data. The event can pass that required data to the listener interface.
 *
 * e.g :
 *
 * interface SomeListener {
 *   fun listen(int value)
 * }
 *
 * class SomeEvent(val int valueToPass) : Event<SomeListener> {
 *   fun notify(listener: SomeListener) {
 *      listener.listen(valueToPass);
 *   }
 * }
 *
 * The EventManager object will send the events to the appropriate listeners.
 */
interface Event<in T> {
    fun notify(listener: T)
}