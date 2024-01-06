package br.barizza.barizzaminigames.protectthechicken.commands

import br.barizza.barizzaminigames.protectthechicken.container.ChickenOwnersContainer
import br.barizza.barizzaminigames.protectthechicken.entity.EntityHolder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin

class GiveChickenExecutor(val plugin: JavaPlugin) : CommandExecutor {
    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        commandAlias: String,
        arguments: Array<out String>
    ): Boolean {
        if(commandSender.hasPermission("ptc.main")) {
            if (arguments.isEmpty()) {
                commandSender.sendMessage("Please, provide a player nick")
                return false
            }
            val player = commandSender.server.getPlayer(arguments[0])

            if (player == null) {
                commandSender.sendMessage("Non existent player")
                return false
            }

            val entity = player.world.spawnEntity(player.location, EntityType.CHICKEN)
            entity.customName = "Galinha de ${player.name}"

            val entityHolder = EntityHolder(player, entity)
            entityHolder.startFollowingTask()

            ChickenOwnersContainer.connectedList.add(entityHolder)

            return true
        }

        return false
    }
}