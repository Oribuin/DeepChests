package xyz.oribuin.upgradeablechests.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.manager.DataManager
import xyz.oribuin.upgradeablechests.manager.TierManager
import xyz.oribuin.upgradeablechests.util.PluginUtils

class BlockListeners(private val plugin: UpgradeableChests) : Listener {

    private val dataManager = this.plugin.getManager(DataManager::class.java)
    private val tierManager = this.plugin.getManager(TierManager::class.java)


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun BlockPlaceEvent.onPlace() {

        // Check if the item has the NBT Value
        if (!PluginUtils.hasValue(this.itemInHand, "upgradeablechests.tier")) return

        val tierID = PluginUtils.getNBTString(this.itemInHand, "upgradeablechests.tier").toIntOrNull() ?: return
        val tier = tierManager.getTier(tierID)

        dataManager.createChest(tier, this.block.location)

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun BlockBreakEvent.onBreak() {

        val chest = dataManager.getChest(this.block.location) ?: return
        this.isCancelled = true


    }

}