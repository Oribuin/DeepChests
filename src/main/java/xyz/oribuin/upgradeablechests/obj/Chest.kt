package xyz.oribuin.upgradeablechests.obj

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import xyz.oribuin.gui.Item
import xyz.oribuin.upgradeablechests.UpgradeableChests

class Chest(var tier: Tier, val location: Location) {

    var items = mutableListOf<ItemStack>()

    val hasItems: Boolean
        get() = items.size > 0

}