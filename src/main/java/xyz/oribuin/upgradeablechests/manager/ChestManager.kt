package xyz.oribuin.upgradeablechests.manager

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.obj.Chest
import xyz.oribuin.upgradeablechests.util.PluginUtils.compressItemStacks
import xyz.oribuin.upgradeablechests.util.PluginUtils.decompressItemStacks

class ChestManager(private val plugin: UpgradeableChests) : Manager(plugin) {

    private lateinit var tierManager: TierManager

    override fun enable() {
        tierManager = this.plugin.getManager(TierManager::class.java)
    }

    private fun createChestItem(chest: Chest): ItemStack? {
        val item = chest.tier.displayItem

        val meta = item.itemMeta ?: return null
        val container = meta.persistentDataContainer
        this.setPDC(chest, container)
        item.itemMeta = meta

        return item
    }

    fun getChestFromBlock(block: org.bukkit.block.Chest): Chest {

        val container = block.persistentDataContainer

        val tier = tierManager.getTier(container.getOrDefault(NamespacedKey(plugin, "tier"), PersistentDataType.INTEGER, 1))
        val items = decompressItemStacks(container.get(NamespacedKey(plugin, "items"), PersistentDataType.BYTE_ARRAY))

        val chest = Chest(tier, block.location)
        chest.items = items.toMutableList()
        return chest
    }

    fun isUpgradeableChest(block: org.bukkit.block.Chest): Boolean {
        return block.persistentDataContainer.keys.contains(NamespacedKey(plugin, "tier"))
    }

    fun getChestFromItem(item: ItemStack): Chest? {

        val meta = item.itemMeta ?: return null
        val container = meta.persistentDataContainer

        val tier = tierManager.getTier(container.getOrDefault(NamespacedKey(plugin, "tier"), PersistentDataType.INTEGER, 1))
        val items = decompressItemStacks(container.get(NamespacedKey(plugin, "items"), PersistentDataType.BYTE_ARRAY))

        val chest = Chest(tier, null)
        chest.items = items.toMutableList()
        return chest
    }

    fun setPDC(chest: Chest, container: PersistentDataContainer) {
        container.set(NamespacedKey(plugin, "tier"), PersistentDataType.INTEGER, chest.tier.id)
        container.set(NamespacedKey(plugin, "items"), PersistentDataType.BYTE_ARRAY, compressItemStacks(chest.items.toTypedArray()))
    }

    fun createItemFromBlock(block: org.bukkit.block.Chest): ItemStack {
        return this.createChestItem(this.getChestFromBlock(block)) ?: return ItemStack(Material.AIR)
    }

    override fun disable() {

    }

}