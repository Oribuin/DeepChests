package xyz.oribuin.upgradeablechests.hook

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import xyz.oribuin.upgradeablechests.obj.ProtectionHook

object SuperiorSBHook : ProtectionHook {

    override fun canBuild(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) return true

        val island = SuperiorSkyblockAPI.getIslandAt(loc) ?: return true

        return island.hasPermission(SuperiorSkyblockAPI.getPlayer(player), IslandPrivilege.getByName("BUILD"))
    }

    override fun canUse(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")) return true

        val island = SuperiorSkyblockAPI.getIslandAt(loc) ?: return true

        return island.hasPermission(SuperiorSkyblockAPI.getPlayer(player), IslandPrivilege.getByName("INTERACT"))
    }



}