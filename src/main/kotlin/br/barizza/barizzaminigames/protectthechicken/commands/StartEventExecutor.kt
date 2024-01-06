package br.barizza.barizzaminigames.protectthechicken.commands

import br.barizza.barizzaminigames.protectthechicken.events.custom.GateEvent
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.event.Event

class StartEventExecutor : CommandExecutor, TabCompleter {
    private val eventMap = mutableMapOf<String, Event>(
        "GateEvent" to GateEvent(),
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender.hasPermission("ptc.main")) {
            if(args.isEmpty()) {
                return false
            }

            val event = eventMap[args[0]] ?: run {
                sender.spigot().sendMessage(*ComponentBuilder().color(ChatColor.RED).append("event ${args[0]} don't exists").create())
                return false
            }

            Bukkit.getServer().pluginManager.callEvent(event)

            return true
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        if(args.size <= 1) {
            val list = mutableListOf<String>()

            eventMap.forEach {
                list.add(it.key)
            }
        }
        return mutableListOf()
    }
}