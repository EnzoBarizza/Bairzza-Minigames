package br.barizza.barizzaminigames.events

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class SemiImplementedEvent : Event(), Cancellable {
    companion object {
        @JvmStatic
        var HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

    var isCanceled = false

    override fun isCancelled(): Boolean {
        return isCanceled
    }

    override fun setCancelled(cancel: Boolean) {
        isCanceled = cancel
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}