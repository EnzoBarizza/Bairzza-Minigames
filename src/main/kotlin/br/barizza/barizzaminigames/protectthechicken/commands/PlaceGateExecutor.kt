package br.barizza.barizzaminigames.protectthechicken.commands

import br.barizza.barizzaminigames.protectthechicken.container.GateEventContainer
import br.barizza.barizzaminigames.protectthechicken.events.GateEventListener
import br.barizza.barizzaminigames.state.GameState
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PlaceGateExecutor : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender.hasPermission("ptc.main")) {
            if(GameState.pluginState == GameState.States.IDLE) {
                val player = sender.server.getPlayer(sender.name) ?: run {

                    sender.sendMessage("You are null wtf")
                    return false
                }

                if(GateEventListener.enablePlayersList.contains(player)) {
                    sender.sendMessage("§cStopped placing blocks of the gate")
                    GateEventListener.enablePlayersList.remove(player)
                    GateEventContainer.saveToDisk()
                } else {
                    sender.sendMessage("§aStarted placing blocks of the gate")
                    GateEventListener.enablePlayersList.add(player)
                }
            } else {
                sender.sendMessage("Cannot place gate blocks mid game")
            }

            return true
        }

        return false
    }
}