package xyz.oribuin.upgradeablechests.obj

import org.bukkit.Location
import org.bukkit.entity.Player

interface ProtectionHook {

    fun canBuild(player: Player, loc: Location): Boolean

    fun canUse(player: Player, loc: Location): Boolean

}