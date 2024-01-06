package br.barizza.barizzaminigames.protectthechicken.container

import br.barizza.barizzaminigames.protectthechicken.container.interfaces.ConfigurableContainer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

object GateEventContainer : ConfigurableContainer("gateEventSavefile.yml") {
    var blockList: MutableMap<Location, Material> = mutableMapOf()

    override fun saveToDisk() {
        val listOfLocation = mutableListOf<Location>()
        val listOfMaterialName = mutableListOf<String>()

        blockList.forEach {
            listOfLocation.add(it.key)
            listOfMaterialName.add(it.value.name)
        }
        config.set("locations", listOfLocation)
        config.set("materials", listOfMaterialName)
        config.save(file)
    }

    override fun loadFromDisk() {
        val listOfLocation: MutableList<*>? = config.get("locations") as MutableList<*>?
        val listOfMaterialName: MutableList<*>? = config.get("materials") as MutableList<*>?

        listOfLocation?.forEachIndexed { index, it ->
            if(listOfMaterialName != null) {
                if(it is Location && listOfMaterialName[index] is String) {
                    blockList[it] = Material.getMaterial(listOfMaterialName[index] as String) ?: run {
                        Bukkit.getLogger().warning("null material ${listOfMaterialName[index] as String}")
                        return
                    }
                }
            }
        }

    }
}