package xyz.oribuin.upgradeablechests.listener

import org.bukkit.Bukkit
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.gui.BreakChestGUI
import xyz.oribuin.upgradeablechests.gui.ChestGUI
import xyz.oribuin.upgradeablechests.hook.IridiumHook
import xyz.oribuin.upgradeablechests.hook.SuperiorSBHook
import xyz.oribuin.upgradeablechests.hook.TownyHook
import xyz.oribuin.upgradeablechests.hook.WGHook
import xyz.oribuin.upgradeablechests.manager.ItemManager
import xyz.oribuin.upgradeablechests.manager.TierManager
import xyz.oribuin.upgradeablechests.util.PluginUtils

class BlockListeners(private val plugin: UpgradeableChests) : Listener {

    private val tierManager = this.plugin.getManager(TierManager::class.java)
    private val itemManager = this.plugin.getManager(ItemManager::class.java)

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun PlayerInteractEvent.onInteract() {
        if (!this.hasBlock()) return

        val block = this.clickedBlock ?: return

        if (this.hand != EquipmentSlot.HAND) return
        if (this.action == Action.LEFT_CLICK_BLOCK) return

        if (block.state !is Chest) return
        val chestBlock = block.state as Chest

        val chest = itemManager.getChestFromBlock(chestBlock) ?: return

        // Check Protection Plugins
        if (!WGHook.canBuild(player, block.location)
            || !IridiumHook.canBuild(player, block.location)
            || !SuperiorSBHook.canBuild(player, block.location)
            || !TownyHook.canBuild(player, block.location)
        )
            return

        this.isCancelled = true
        ChestGUI(plugin, chest, player)
    }


//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    fun BlockPlaceEvent.onPlace() {
//        // Check if the item has the NBT Value
//        val loc = this.block.location
//
//
//        // Check Protection Plugins
//        if (!WGHook.canBuild(player, loc)
//            || !IridiumHook.canBuild(player, loc)
//            || !SuperiorSBHook.canBuild(player, loc)
//            || !TownyHook.canBuild(player, loc)
//        )
//            return
//
//        val chest = itemManager.getChestFromBlock(this.block.state as Chest) ?: return
//
//    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun BlockBreakEvent.onBreak() {

        val loc = this.block.location

        val chest = itemManager.getChestFromBlock(this.block.state as Chest) ?: return

        // Check Protection Plugins
        if (!WGHook.canBuild(player, loc)
            || !IridiumHook.canBuild(player, loc)
            || !SuperiorSBHook.canBuild(player, loc)
            || !TownyHook.canBuild(player, loc)
        )
            return


        this.isCancelled = true
        BreakChestGUI(plugin, player, chest)

    }

    init {
        Bukkit.getPluginManager().registerEvents(this, this.plugin)
    }

}