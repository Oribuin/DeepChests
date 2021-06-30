package xyz.oribuin.upgradeablechests.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.gui.BreakChestGUI
import xyz.oribuin.upgradeablechests.hook.IridiumHook
import xyz.oribuin.upgradeablechests.hook.SuperiorSBHook
import xyz.oribuin.upgradeablechests.hook.TownyHook
import xyz.oribuin.upgradeablechests.hook.WGHook
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

        val tierID = PluginUtils.getNBTInt(this.itemInHand, "upgradeablechests.tier")

        val tier = tierManager.getTier(tierID)
        val loc = this.block.location

        // Check Protection Plugins
        if (!WGHook.canBuild(player, loc)
            || !IridiumHook.canBuild(player, loc)
            || !SuperiorSBHook.canBuild(player, loc)
            || !TownyHook.canBuild(player, loc)
        )
            return

        dataManager.createChest(0, tier, loc)

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun BlockBreakEvent.onBreak() {

        val loc = this.block.location

        // Check Protection Plugins
        if (!WGHook.canBuild(player, loc)
            || !IridiumHook.canBuild(player, loc)
            || !SuperiorSBHook.canBuild(player, loc)
            || !TownyHook.canBuild(player, loc)
        )
            return

        val chest = dataManager.getChest(this.block.location) ?: return
        this.isCancelled = true
        BreakChestGUI(plugin, player, chest)

    }

    init {
        Bukkit.getPluginManager().registerEvents(this, this.plugin)
    }
}