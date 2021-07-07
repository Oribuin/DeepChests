package xyz.oribuin.upgradeablechests

import xyz.oribuin.orilibrary.OriPlugin
import xyz.oribuin.upgradeablechests.command.CmdChest
import xyz.oribuin.upgradeablechests.hook.PAPI
import xyz.oribuin.upgradeablechests.listener.BlockListeners
import xyz.oribuin.upgradeablechests.manager.ItemManager
import xyz.oribuin.upgradeablechests.manager.MessageManager
import xyz.oribuin.upgradeablechests.manager.TierManager

class UpgradeableChests : OriPlugin() {

    override fun enablePlugin() {

        // Load plugin managers asynchronously
        server.scheduler.runTaskAsynchronously(this, Runnable {
            this.getManager(ItemManager::class.java)
            this.getManager(TierManager::class.java)
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

    }

    override fun disablePlugin() {}

}