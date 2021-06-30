package xyz.oribuin.upgradeablechests

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import xyz.oribuin.orilibrary.OriPlugin
import xyz.oribuin.orilibrary.util.FileUtils
import xyz.oribuin.upgradeablechests.hook.PAPI
import xyz.oribuin.upgradeablechests.manager.DataManager

class UpgradeableChests : OriPlugin() {

    lateinit var breakChestConfig: FileConfiguration

    override fun enablePlugin() {

        // Load plugin managers asynchronously
        server.scheduler.runTaskAsynchronously(this, Runnable {
            this.getManager(DataManager::class.java)
        })


        if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PAPI.Expansion(this).register()
        }

        this.breakChestConfig = YamlConfiguration.loadConfiguration(FileUtils.createMenuFile(this, "break-chest-menu"))
    }

    override fun disablePlugin() {}

}