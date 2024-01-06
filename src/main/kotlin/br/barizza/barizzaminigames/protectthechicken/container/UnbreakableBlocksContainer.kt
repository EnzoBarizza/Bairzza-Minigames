package br.barizza.barizzaminigames.protectthechicken.container

import br.barizza.barizzaminigames.protectthechicken.container.interfaces.ConfigurableContainer
import org.bukkit.Material

object UnbreakableBlocksContainer : ConfigurableContainer("unbreakableBlocks.yml") {
    var materialsList: MutableList<Material> = mutableListOf()

    override fun saveToDisk() {
        val materials = mutableListOf<String>()
        materialsList.forEach { materials.add(it.name) }

        config.set("materials", materials)
        config.save(file)
    }

    override fun loadFromDisk() {
        val materials = config.get("materials") as MutableList<*>?
        materials?.forEach {
            if (it is String) {
                val material = Material.getMaterial(it)

                if(material != null) {
                    materialsList.add(material)
                }
            }
        }
    }
}