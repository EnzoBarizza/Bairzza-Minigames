package br.barizza.barizzaminigames.protectthechicken.events

import br.barizza.barizzaminigames.protectthechicken.container.ChickenOwnersContainer
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ChickenEventListener : Listener {
    private val playersWaitingChickenNameChanges = mutableListOf<String>()
    @EventHandler
    fun changeChickenNameByPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) {
        if (!playersWaitingChickenNameChanges.contains(event.player.name) && event.rightClicked.hasMetadata("owner")) {
            if(event.rightClicked.getMetadata("owner")[0].asString() == event.player.name) {
                event.player.sendMessage("§a Type the new name of the chicken:")
                playersWaitingChickenNameChanges.add(event.player.name)
            }
        }
    }

    @EventHandler
    fun getNewChickenNameByAsyncPlayerChatEvent(event: AsyncPlayerChatEvent) {
        if (playersWaitingChickenNameChanges.contains(event.player.name) && ChickenOwnersContainer.connectedList.any { it.owner.name == event.player.name }) {
            ChickenOwnersContainer.connectedList.find { it.owner.name == event.player.name }?.entity?.customName = event.message
            playersWaitingChickenNameChanges.remove(event.player.name)

            event.isCancelled = true
        }
    }

    @EventHandler
    fun handleChickenDieByEntityDeathEvent(event: EntityDeathEvent) {
        val entity = event.entity
        val server = entity.server

        entity.getMetadata("owner").forEach {
            val player = event.entity.server.getPlayer(it.asString())
            for (i in 1..20) {
                val tnt: TNTPrimed? = player?.world?.spawnEntity(player.location, EntityType.PRIMED_TNT) as TNTPrimed?
                tnt?.fuseTicks = 60
            }
            player?.gameMode = GameMode.SPECTATOR

            server.onlinePlayers.forEach { itPlayer ->
                itPlayer.sendTitle("§a${entity.customName} §a§lMORREU", "§cR.I.P ${it.asString()}", 20, 100, 20)
                itPlayer.playSound(itPlayer, "minecraft:entity.chicken.death", 10000f, 1f)
            }
            ChickenOwnersContainer.connectedList.removeIf { holder -> holder.owner.name == it.asString() }
        }
    }

    @EventHandler
    fun handleChickenDamageByEntityDamageEvent(event: EntityDamageEvent) {
        if (event.entity.hasMetadata("owner")) {
            val owner = event.entity.getMetadata("owner")[0].asString()

            if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && event.entity.server.onlinePlayers.any { it.name == owner }) {
                event.isCancelled = true
                return
            }

            event.damage = 0.5

            event.entity.server.getPlayer(owner)?.spigot()?.sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent("§c${(((event.entity) as LivingEntity).health / 0.5) - 1}/8")
            )
        }
    }

    @EventHandler
    fun saveChickenAtRuntimeByPlayerQuitEvent(event: PlayerQuitEvent) {
        val holder = ChickenOwnersContainer.connectedList.find { it.owner.name == event.player.name } ?: return

        holder.stopFollowingTask()
        ChickenOwnersContainer.connectedList.remove(holder)
        ChickenOwnersContainer.disconnectedList.add(holder)
    }

    @EventHandler
    fun loadChickenAtRuntimeByPlayerJoinEvent(event: PlayerJoinEvent) {
        val holder = ChickenOwnersContainer.disconnectedList.find { it.owner.name == event.player.name } ?: return

        holder.owner = event.player

        holder.startFollowingTask()

        ChickenOwnersContainer.disconnectedList.remove(holder)
        ChickenOwnersContainer.connectedList.add(holder)
    }
}