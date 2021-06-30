package xyz.oribuin.upgradeablechests.obj

import org.bukkit.ChatColor
import org.bukkit.Material
import xyz.oribuin.gui.Item

class Tier(val id: Int) {
    var slots = 100
    var displayName = "Tier $id"
    var displayItem = Item.Builder(Material.CHEST)
        .setName("${ChatColor.GREEN}Tier $id Chest")
        .setLore(listOf("${ChatColor.GRAY}Click the floor to place down",
            "${ChatColor.GRAY}This tier $id upgradeable chest",
            " ",
            "${ChatColor.GRAY}This chest has ${ChatColor.GREEN}x$slots ${ChatColor.GRAY}slots!"
        ))
        .setNBT("upgradeablechest.tier", id)
        .glow()
        .create()
}