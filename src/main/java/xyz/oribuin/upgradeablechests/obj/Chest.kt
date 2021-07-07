package xyz.oribuin.upgradeablechests.obj

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Chest(var tier: Tier, var location: Location?) {

    var items = mutableListOf(ItemStack(Material.AIR))

    val hasItems: Boolean
        get() = items.any { it.type != Material.AIR }

}