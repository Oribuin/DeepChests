package xyz.oribuin.upgradeablechests.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.ItemStack
import xyz.oribuin.gui.PaginatedGui
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.manager.DataManager
import xyz.oribuin.upgradeablechests.obj.Chest

class ChestGUI(private val plugin: UpgradeableChests, private val chest: Chest, player: Player) {

    private val placeActions = setOf(InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ONE_CURSOR)
    private val takeActions = setOf(InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.HOTBAR_SWAP)


    init {

        val pageSlots = mutableListOf<Int>()
        for (i in 0..44) pageSlots.add(i)

        val gui = PaginatedGui(54, "Tier ${chest.tier.id} Chest", pageSlots)
        val items = mutableListOf<ItemStack>()
        items.addAll(chest.items)

        gui.setCloseAction {

            pageSlots.forEach { slot ->
                this.
            }

            chest.items = items
            this.plugin.getManager(DataManager::class.java).saveChestItems(chest)
        }

        gui.setDefaultClickFunction {

            if (!pageSlots.contains(it.slot)) {
                it.isCancelled = true
                it.result = Event.Result.DENY

                (it.whoClicked as Player).updateInventory()
                return@setDefaultClickFunction
            }

            it.inventory.

        }

        chest.items.forEach { itemStack -> gui.addItem(itemStack) { } }

        gui.open(player)
    }

}