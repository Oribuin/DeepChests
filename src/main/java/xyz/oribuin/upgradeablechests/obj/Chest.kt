package xyz.oribuin.upgradeablechests.obj

import org.bukkit.Location
import org.bukkit.inventory.ItemStack

class Chest(val id: Int, var tier: Tier, val location: Location) {

    var items = mutableListOf<ItemStack>()

}