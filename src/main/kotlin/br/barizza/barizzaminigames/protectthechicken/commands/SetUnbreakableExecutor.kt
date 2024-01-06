package br.barizza.barizzaminigames.protectthechicken.commands

import br.barizza.barizzaminigames.protectthechicken.container.UnbreakableBlocksContainer
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SetUnbreakableExecutor : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.hasPermission("ptc.main")) {
            val player = sender.server.getPlayer(sender.name) ?: run {
                sender.sendMessage("You are null wtf")
                return true
            }

            val itemInUse = player.inventory.itemInMainHand
            if (itemInUse.type == Material.AIR || itemInUse.type == Material.CAVE_AIR || itemInUse.type == Material.VOID_AIR) {
                sender.sendMessage("You need to be holding something")
                return true
            }

            if (UnbreakableBlocksContainer.materialsList.contains(itemInUse.type)) {
                val component = ComponentBuilder("The block ")
                    .color(ChatColor.RED)
                    .append(TranslatableComponent(itemInUse.translationKey))
                    .append(" Was removed as unbreakable block")
                    .create()

                sender.spigot().sendMessage(*component)
                UnbreakableBlocksContainer.materialsList.remove(itemInUse.type)
            } else {
                val component = ComponentBuilder("The block ")
                    .color(ChatColor.GREEN)
                    .append(TranslatableComponent(itemInUse.translationKey))
                    .append(" Was added as unbreakable block")
                    .create()

                sender.spigot().sendMessage(*component)
                UnbreakableBlocksContainer.materialsList.add(itemInUse.type)
            }

            UnbreakableBlocksContainer.saveToDisk()
            return true
        }
        return false
    }
}