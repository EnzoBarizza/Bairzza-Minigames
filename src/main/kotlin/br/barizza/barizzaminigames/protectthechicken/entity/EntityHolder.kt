package br.barizza.barizzaminigames.protectthechicken.entity

import br.barizza.barizzaminigames.BarizzaMinigames
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import kotlin.math.cos
import kotlin.math.sin

class EntityHolder(var owner: Player, val entity: Entity) {
    private var taskId: Int = -1

    val followTask = Runnable {
        val newLocation = owner.location

        val calcYaw = owner.location.yaw + 180

        newLocation.x -= cos(Math.toRadians(calcYaw.toDouble()))
        newLocation.z -= sin(Math.toRadians(calcYaw.toDouble()))

        entity.teleport(newLocation)
    }

    init {
        entity.setMetadata("owner", FixedMetadataValue(BarizzaMinigames.INSTANCE, owner.name))
    }

    fun startFollowingTask() {
        val newId = Bukkit.getScheduler().scheduleSyncRepeatingTask(BarizzaMinigames.INSTANCE, followTask, 1, 1)
        if(newId < 0) {
            throw InstantiationError("Cannot instantiate a follow task for player ${owner.name}")
        } else {
            taskId = newId
        }
    }

    fun stopFollowingTask() {
        if(taskId >= 0) {
            Bukkit.getScheduler().cancelTask(taskId)
            taskId = -1
        }
    }
}