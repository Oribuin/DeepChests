package xyz.oribuin.upgradeablechests.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import xyz.oribuin.gui.Gui
import xyz.oribuin.gui.Item
import xyz.oribuin.orilibrary.util.HexUtils.colorify
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.obj.Chest

class BreakChestGUI(private val plugin: UpgradeableChests, private val player: Player, private val chest: Chest) {

    init {

        val gui = Gui(27, this.plugin.breakChestConfig.getString("menu-name") ?: "Are you sure?")
        gui.setDefaultClickFunction {
            it.isCancelled = true
            it.result = Event.Result.DENY

            (it as Player).updateInventory()
        }

        gui.setItems(mutableListOf(0, 8, 18, 26), Item.Builder(Material.RED_STAINED_GLASS_PANE).setName(" ").create()) { }
        gui.setItems(mutableListOf(1, 7, 9, 17, 19, 25), Item.Builder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").create()) { }

        gui.setItem(12, Item.Builder(Material.LIME_DYE)
            .setName(colorify("&a&lConfirm"))
            .setLore(colorify("&7Click to destroy the chest."))
            .create()) {

            if (chest.hasItems) {
                it.whoClicked.closeInventory()
                return@setItem
            }

        }

        gui.setItem(14, Item.Builder(Material.RED_DYE)
            .setName(colorify("&c&lDeny"))
            .setLore(colorify("&7Cancel chest destruction."))
            .create()) {

            if (chest.hasItems) {
                // TODO, Add message
                return@setItem
            }

            it.whoClicked.closeInventory()

        }
    }

}