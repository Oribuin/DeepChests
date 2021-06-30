package xyz.oribuin.upgradeablechests.hook

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import xyz.oribuin.upgradeablechests.obj.ProtectionHook

object WGHook : ProtectionHook {

    override fun canBuild(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return true

        val worldGuard = WorldGuard.getInstance()
        val query = worldGuard.platform.regionContainer.createQuery()

        return query.testState(BukkitAdapter.adapt(loc), WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD)
    }

    override fun canUse(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) return true

        val worldGuard = WorldGuard.getInstance()
        val query = worldGuard.platform.regionContainer.createQuery()

        return query.testState(BukkitAdapter.adapt(loc), WorldGuardPlugin.inst().wrapPlayer(player), Flags.USE)

    }

}