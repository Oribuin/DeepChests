package xyz.oribuin.upgradeablechests.obj

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Tier(val id: Int) {
    var slots = 100
    var displayName = "Tier $id"
    var displayItem: ItemStack = ItemStack(Material.CHEST)
}