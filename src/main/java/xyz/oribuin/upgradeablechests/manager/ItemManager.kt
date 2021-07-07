package xyz.oribuin.upgradeablechests.manager

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.obj.Chest
import xyz.oribuin.upgradeablechests.util.PluginUtils

class ItemManager(val plugin: UpgradeableChests) : Manager(plugin) {

    override fun enable() {

    }

    fun createChestItem(chest: Chest): ItemStack? {
        val item = chest.tier.displayItem

        val meta = item.itemMeta ?: return null
        val container = meta.persistentDataContainer

        container.set(NamespacedKey(plugin, "items"), PersistentDataType.BYTE_ARRAY, PluginUtils.compressItemStacks(chest.items.toTypedArray()))
        container.set(NamespacedKey(plugin, "tier"), PersistentDataType.INTEGER, chest.tier.id)

        item.itemMeta = meta

        return item
    }

    fun getChestFromBlock(block: org.bukkit.block.Chest): Chest? {

        val container = block.persistentDataContainer

        val tier = this.plugin.getManager(TierManager::class.java).getTier(container.get(NamespacedKey(plugin, "tier"), PersistentDataType.INTEGER) ?: return null)
        val byteArray = container.get(NamespacedKey(plugin, "items"), PersistentDataType.BYTE_ARRAY) ?: return null

        val items = PluginUtils.decompressItemStacks(byteArray)

        val chest = Chest(tier, block.location)
        chest.items = items.toMutableList()
        return chest

    }

    fun createItemFromBlock(block: org.bukkit.block.Chest): ItemStack {
        return this.createChestItem(this.getChestFromBlock(block) ?: return ItemStack(Material.AIR)) ?: return ItemStack(Material.AIR)
    }

    override fun disable() {

    }

}