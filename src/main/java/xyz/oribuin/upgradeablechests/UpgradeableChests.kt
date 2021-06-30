package xyz.oribuin.upgradeablechests

import xyz.oribuin.orilibrary.OriPlugin
import xyz.oribuin.upgradeablechests.manager.DataManager

class UpgradeableChests : OriPlugin() {

    override fun enablePlugin() {

        // Load plugin managers asynchronously
        server.scheduler.runTaskAsynchronously(this, Runnable {
            this.getManager(DataManager::class.java)
        })

    }

    override fun disablePlugin() {}

}