package xyz.oribuin.upgradeablechests.obj

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.oribuin.gui.Item
import java.util.*

class Tier(val id: Int) {
    var slots = 100
    var displayName = "Tier $id"
    var displayItem: ItemStack = Item.Builder(Material.CHEST)
        .setName("${ChatColor.GREEN}Tier $id Chest")
        .setLore(listOf("${ChatColor.GRAY}Place this on the floor to",
            "${ChatColor.GRAY}create a tier $id upgradeable chest!",
            " ",
            "${ChatColor.GRAY}This chest has ${ChatColor.GREEN}x$slots ${ChatColor.GRAY}slots!"
        ))
        .setNBT("upgradeablechests.tier", id.toString())
//        .setNBT("upgradeablechests.id", UUID.randomUUID())
        .glow()
        .create()
}