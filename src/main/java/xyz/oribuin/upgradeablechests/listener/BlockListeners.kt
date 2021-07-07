package xyz.oribuin.upgradeablechests.listener

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Chest
import org.bukkit.entity.Player
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
import xyz.oribuin.upgradeablechests.manager.ChestManager

class BlockListeners(private val plugin: UpgradeableChests) : Listener {

    private val chestManager = this.plugin.getManager(ChestManager::class.java)

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun PlayerInteractEvent.onInteract() {
        if (!this.hasBlock()) return

        val block = this.clickedBlock ?: return

        if (this.hand != EquipmentSlot.HAND) return
        if (this.action != Action.RIGHT_CLICK_BLOCK) return

        if (block.state !is Chest)
            return

        val chestBlock = block.state as Chest
        if (!chestManager.isUpgradeableChest(chestBlock))
            return

        val chest = chestManager.getChestFromBlock(chestBlock)

        // Check Protection Plugins
        if (cantBuild(player, block.location))
            return

        chestBlock.open()
        this.isCancelled = true
        ChestGUI(plugin, chest, player, chestBlock)
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun BlockPlaceEvent.onPlace() {
        // Check if the item has the NBT Value
        val loc = this.block.location

        if (block.state !is Chest)
            return

        val chestBlock = block.state as Chest

        if (!chestManager.isUpgradeableChest(chestBlock))
            return

        val chest = chestManager.getChestFromItem(this.itemInHand) ?: return

        // Check Protection Plugins
        if (cantBuild(player, loc))
            return

        chest.location = this.block.location
        chestManager.setPDC(chest, chestBlock.persistentDataContainer)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun BlockBreakEvent.onBreak() {
        val loc = this.block.location

        if (this.block.state !is Chest) {
            return
        }

        val blockChest = this.block.state as Chest

        if (!chestManager.isUpgradeableChest(blockChest))
            return

        val chest = chestManager.getChestFromBlock(blockChest)

        // Check Protection Plugins
        if (cantBuild(player, loc))
            return

        if (player.isSneaking) {
            chestManager.createItemFromBlock(blockChest)
            return
        }

        this.isCancelled = true
        BreakChestGUI(plugin, player, chest)

    }

    private fun cantBuild(player: Player, loc: Location): Boolean {
        return !WGHook.canBuild(player, loc) || !IridiumHook.canBuild(player, loc) || !SuperiorSBHook.canBuild(player, loc) || !TownyHook.canBuild(player, loc)
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, this.plugin)
    }

}