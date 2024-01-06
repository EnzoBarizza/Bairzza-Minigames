package br.barizza.barizzaminigames.protectthechicken.container.interfaces

import br.barizza.barizzaminigames.BarizzaMinigames
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class ConfigurableContainer(val fileNameWithExtension: String) {
    protected var file: File = File(BarizzaMinigames.INSTANCE.dataFolder, fileNameWithExtension)
    protected var config: YamlConfiguration

    init {
        if(!file.exists()) BarizzaMinigames.INSTANCE.saveResource(fileNameWithExtension, false)

        config = YamlConfiguration()
        config.options().parseComments(true)

        config.load(file)
    }

    abstract fun saveToDisk()

    abstract fun loadFromDisk()

}