package br.barizza.barizzaminigames.protectthechicken.events

import br.barizza.barizzaminigames.protectthechicken.container.GateEventContainer
import br.barizza.barizzaminigames.protectthechicken.container.UnbreakableBlocksContainer
import br.barizza.barizzaminigames.protectthechicken.events.custom.GateEvent
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class GateEventListener : Listener {
    companion object {
        val enablePlayersList: MutableList<Player> = mutableListOf()
    }

    @EventHandler
    fun handleGateBlockPlaceByBlockPlaceEvent(event: BlockPlaceEvent) {
        if (enablePlayersList.contains(event.player)) {
            if (!GateEventContainer.blockList.contains(event.block.location)) {
                GateEventContainer.blockList[event.block.location] = event.block.type
                event.player.sendMessage("§aBlock Placed At x = ${event.block.x} y = ${event.block.y} z = ${event.block.z}")
            }
        }
    }

    @EventHandler
    fun handleGateEventBreakByBlockBreakEvent(event: BlockBreakEvent) {
        if (enablePlayersList.contains(event.player)) {
            GateEventContainer.blockList.remove(event.block.location)
            event.player.sendMessage("§cBlock Broken At x = ${event.block.x} y = ${event.block.y} z = ${event.block.z}")
        }

        if(UnbreakableBlocksContainer.materialsList.contains(event.block.type)) {
            event.isCancelled = true
        }
    }


    @EventHandler
    fun handleGateEvent(event: GateEvent) {
        GateEventContainer.blockList.forEach {
            val block = it.key.block

            if (block.type != Material.AIR) {
                block.world.spawnParticle(
                    Particle.BLOCK_CRACK,
                    it.key,
                    100,
                    0.5,
                    0.5,
                    0.5,
                    block.blockData
                )
                block.world.playSound(block.location, block.blockData.soundGroup.breakSound, SoundCategory.BLOCKS, 100000f, 0.8f)
                block.type = Material.AIR
            } else {
                block.world.playSound(block.location, it.value.createBlockData().soundGroup.placeSound, SoundCategory.BLOCKS, 100000f, 0.8f)
                block.type = it.value
            }
        }
    }


}

