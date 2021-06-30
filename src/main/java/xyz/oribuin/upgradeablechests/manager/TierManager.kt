package xyz.oribuin.upgradeablechests.manager

import org.bukkit.Material
import xyz.oribuin.gui.Item
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.orilibrary.util.HexUtils.colorify
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.obj.Tier

class TierManager(private val plugin: UpgradeableChests) : Manager(plugin) {

    val tiers = mutableMapOf<Int, Tier>()
    override fun enable() {
        val section = this.plugin.config.getConfigurationSection("tiers") ?: return

        // Cache all the plugin's tiers.
        section.getKeys(false).forEach { s ->
            val id = s.toIntOrNull() ?: return@forEach
            val tier = Tier(id)
            tier.slots = section.getInt("$s.slots")

            if (section.getString("$s.displayName") != null) {
                tier.displayName = section.getString("$s.displayName") ?: return@forEach
            }

            tier.displayItem = Item.Builder(Material.matchMaterial(section.getString("$s.item.material") ?: "CHEST") ?: Material.CHEST)
                .setName(colorify(section.getString("$s.item.name")))
                .setLore(section.getStringList("$s.item.lore").map { colorify(it) }.toList())
                .setNBT("upgradeablechest.tier", id)
                .glow()
                .create()

            tiers[s.toInt()] = tier
        }

    }

    /**
     * Get a [Tier] from the tier's ID
     *
     * @param id The id of the tier.
     * @return [Tier]
     */
    fun getTier(id: Int): Tier {
        return this.tiers.getOrDefault(id, defaultTier)
    }

    override fun disable() {
        this.tiers.clear()
    }

    private val defaultTier: Tier
        get() {
            val tier = Tier(1)
            tier.displayName = "Tier 1"
            return tier
        }

}