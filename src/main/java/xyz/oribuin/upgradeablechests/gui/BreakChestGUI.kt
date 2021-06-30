package xyz.oribuin.upgradeablechests.gui

import net.coreprotect.CoreProtect
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import xyz.oribuin.gui.Gui
import xyz.oribuin.gui.Item
import xyz.oribuin.orilibrary.util.HexUtils.colorify
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.manager.DataManager
import xyz.oribuin.upgradeablechests.manager.MessageManager
import xyz.oribuin.upgradeablechests.obj.Chest

class BreakChestGUI(private val plugin: UpgradeableChests, private val player: Player, private val chest: Chest) {

    init {
        val msg = this.plugin.getManager(MessageManager::class.java)

        val gui = Gui(27, "Are you sure?")
        gui.setDefaultClickFunction {
            it.isCancelled = true
            it.result = Event.Result.DENY

            (it.whoClicked as Player).updateInventory()
        }

        gui.setItems(mutableListOf(0, 8, 18, 26), Item.Builder(Material.RED_STAINED_GLASS_PANE).setName(" ").create()) { }
        gui.setItems(mutableListOf(1, 7, 9, 17, 19, 25), Item.Builder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").create()) { }

        gui.setItem(12, Item.Builder(Material.LIME_DYE)
            .setName(colorify("&a&lConfirm"))
            .setLore(colorify("&7Click to destroy the chest."))
            .create()) {

            if (chest.hasItems) {
                msg.send(it.whoClicked, "chest-isnt-empty")
                return@setItem
            }

            msg.send(it.whoClicked, "destroyed-chest")
            val block = chest.location.block

            if (Bukkit.getPluginManager().isPluginEnabled("CoreProtect")) {
                CoreProtect.getInstance().api.logRemoval(player.uniqueId.toString(), block.location, block.type, block.blockData)
            }

            block.type = Material.AIR
            block.location.world?.dropItemNaturally(chest.location, chest.tier.displayItem)

            this.plugin.getManager(DataManager::class.java).deleteChest(chest.id)
            it.whoClicked.closeInventory()

        }

        gui.setItem(14, Item.Builder(Material.RED_DYE)
            .setName(colorify("&c&lDeny"))
            .setLore(colorify("&7Cancel chest destruction."))
            .create()) { it.whoClicked.closeInventory() }

        gui.open(player)
    }

}