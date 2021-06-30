package xyz.oribuin.upgradeablechests.hook

import com.palmergames.bukkit.towny.TownyAPI
import com.palmergames.bukkit.towny.`object`.TownyPermission
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import xyz.oribuin.upgradeablechests.obj.ProtectionHook

object TownyHook : ProtectionHook {

    override fun canBuild(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("Towny")) return true

        if (TownyAPI.getInstance().isWilderness(loc)) return true

        return PlayerCacheUtil.getCachePermission(player, loc, loc.block.type, TownyPermission.ActionType.BUILD)
    }

    override fun canUse(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("Towny")) return true

        if (TownyAPI.getInstance().isWilderness(loc)) return true

        return PlayerCacheUtil.getCachePermission(player, loc, loc.block.type, TownyPermission.ActionType.ITEM_USE)
    }

}