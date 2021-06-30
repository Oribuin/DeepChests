package xyz.oribuin.upgradeablechests

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import xyz.oribuin.orilibrary.OriPlugin
import xyz.oribuin.orilibrary.util.FileUtils
import xyz.oribuin.upgradeablechests.command.CmdChest
import xyz.oribuin.upgradeablechests.hook.PAPI
import xyz.oribuin.upgradeablechests.listener.BlockListeners
import xyz.oribuin.upgradeablechests.manager.DataManager
import xyz.oribuin.upgradeablechests.manager.MessageManager
import xyz.oribuin.upgradeablechests.manager.TierManager
import xyz.oribuin.upgradeablechests.task.ParticleTask

class UpgradeableChests : OriPlugin() {

    override fun enablePlugin() {

        // Load plugin managers asynchronously
        server.scheduler.runTaskAsynchronously(this, Runnable {
            this.getManager(TierManager::class.java)
            this.getManager(DataManager::class.java)
            this.getManager(MessageManager::class.java)
        })

        // Register any PlaceholderAPI Expansion Placeholders.
        if (this.server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PAPI.Expansion(this).register()
        }

        // Register Listeners
        BlockListeners(this)

        // Register Commands
        CmdChest(this).register(null, null)

        // Register Tasks
        ParticleTask(this)
    }

    override fun disablePlugin() {}

}