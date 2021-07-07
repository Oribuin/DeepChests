package xyz.oribuin.upgradeablechests.manager

import org.bukkit.Material
import xyz.oribuin.gui.Item
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.orilibrary.util.HexUtils.colorify
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.obj.Chest
import xyz.oribuin.upgradeablechests.obj.Tier

class TierManager(private val plugin: UpgradeableChests) : Manager(plugin) {

    val tiers = mutableMapOf<Int, Tier>()
    override fun enable() {
        val section = this.plugin.config.getConfigurationSection("tiers") ?: return

        // Cache all the plugin's tiers.
        section.getKeys(false).forEach { s ->
            val tier = Tier(s.toIntOrNull() ?: defaultTier.id)
            tier.slots = section.getInt("$s.slots")

            if (section.getString("$s.displayName") != null) {
                tier.displayName = section.getString("$s.displayName") ?: defaultTier.displayName
            }

            tier.displayItem = Item.Builder(Material.CHEST)
                .setName(colorify(section.getString("$s.item.name")))
                .setLore(section.getStringList("$s.item.lore").map { colorify(it) }.toList())
                .glow()
                .create()

            val pdc = (tier.displayItem.itemMeta ?: return@forEach).persistentDataContainer
            this.plugin.getManager(ChestManager::class.java).setPDC(Chest(tier, null), pdc)

            tiers[tier.id] = tier
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
            tier.displayName = "Tier #1"
            return tier
        }

}