package xyz.oribuin.upgradeablechests.hook

import com.iridium.iridiumskyblock.PermissionType
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import xyz.oribuin.upgradeablechests.obj.ProtectionHook

object IridiumHook : ProtectionHook {

    override fun canBuild(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock")) return true

        val optionalIsland = IridiumSkyblockAPI.getInstance().getIslandViaLocation(loc)
        if (optionalIsland.isEmpty) return true

        val island = optionalIsland.get()
        val user = IridiumSkyblockAPI.getInstance().getUser(player)

        return IridiumSkyblockAPI.getInstance().getIslandPermission(island, user, PermissionType.BLOCK_PLACE)
    }

    override fun canUse(player: Player, loc: Location): Boolean {
        if (!Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock")) return true

        val optionalIsland = IridiumSkyblockAPI.getInstance().getIslandViaLocation(loc)
        if (optionalIsland.isEmpty) return true

        val island = optionalIsland.get()
        val user = IridiumSkyblockAPI.getInstance().getUser(player)

        return IridiumSkyblockAPI.getInstance().getIslandPermission(island, user, PermissionType.OPEN_CONTAINERS)

    }


}