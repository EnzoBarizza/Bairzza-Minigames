package br.barizza.barizzaminigames

import br.barizza.barizzaminigames.protectthechicken.commands.GiveChickenExecutor
import br.barizza.barizzaminigames.protectthechicken.commands.PlaceGateExecutor
import br.barizza.barizzaminigames.protectthechicken.commands.SetUnbreakableExecutor
import br.barizza.barizzaminigames.protectthechicken.commands.StartEventExecutor
import br.barizza.barizzaminigames.protectthechicken.container.GateEventContainer
import br.barizza.barizzaminigames.protectthechicken.container.UnbreakableBlocksContainer
import br.barizza.barizzaminigames.protectthechicken.container.interfaces.ConfigurableContainer
import br.barizza.barizzaminigames.protectthechicken.events.ChickenEventListener
import br.barizza.barizzaminigames.protectthechicken.events.GateEventListener
import br.barizza.barizzaminigames.state.GameState
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class BarizzaMinigames : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: BarizzaMinigames
    }

    init {
        INSTANCE = this
    }

    override fun onEnable() {
        Bukkit.getLogger().log(Level.INFO, "Starting PTC")
        GameState.pluginState = GameState.States.IDLE
        setupCommands()
        registerEvents()
        loadAllConfigsFromDisk(GateEventContainer, UnbreakableBlocksContainer)
    }

    override fun onDisable() {
    }

    private fun setupCommands() {
        getCommand("ptcgivechicken")?.apply { setExecutor(GiveChickenExecutor(this@BarizzaMinigames)) }
        getCommand("ptcplacegate")?.apply { setExecutor(PlaceGateExecutor()) }
        getCommand("ptcstartevent")?.apply {
            val executor = StartEventExecutor()

            setExecutor(executor)
            tabCompleter = executor
        }
        getCommand("ptcsetunbreakable")?.apply { setExecutor(SetUnbreakableExecutor()) }
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(GateEventListener(), this)
        server.pluginManager.registerEvents(ChickenEventListener(), this)
    }

    private fun loadAllConfigsFromDisk(vararg configs: ConfigurableContainer) {
        configs.forEach {
            it.loadFromDisk()
        }
    }
}
