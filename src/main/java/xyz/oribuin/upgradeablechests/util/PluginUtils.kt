package xyz.oribuin.upgradeablechests.util

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import xyz.oribuin.gui.libs.bananapuncher714.nbteditor.NBTEditor
import java.util.*

object PluginUtils {

    /**
     * Deserializes a String into an ItemStack
     *
     * @param serialization The serialized [ItemStack]
     * @return The ItemStack that was serialized.
     */
    fun handleDeserialization(serialization: String): ItemStack? {
        val configuration = YamlConfiguration()
        try {
            configuration.loadFromString(String(Base64.getDecoder().decode(serialization)))
        } catch (exception: InvalidConfigurationException) {
            return null
        }
        return configuration.getItemStack("item-stack")
    }

    /**
     * Serializes the given [ItemStack] to a [String]
     *
     * @param itemStack to be serialized
     * @return A string containing the serialized contents
     */
    fun handleSerialization(itemStack: ItemStack): String {
        val configuration = YamlConfiguration()
        configuration["item-stack"] = itemStack
        return Base64.getEncoder().encodeToString(configuration.saveToString().toByteArray())
    }

    /**
     * Get an [ItemStack]'s NBT String Key Value
     *
     * @param item The [ItemStack]
     * @param key The String key
     * @return The value of the key.
     */
    fun getNBTString(item: ItemStack, key: String): String {
        return NBTEditor.getString(item, key)
    }

    /**
     * Get an [ItemStack]'s NBT Integer Key Value
     *
     * @param item The [ItemStack]
     * @param key The String key
     * @return The value of the key.
     */
    fun getNBTInt(item: ItemStack, key: String): Int {
        return NBTEditor.getInt(item, key)
    }


    /**
     * Checks if an [ItemStack] has a nbt value.
     *
     * @param item The [ItemStack]
     * @param key The String key
     * @return True if the item contains the value.
     */
    fun hasValue(item: ItemStack, key: String): Boolean {
        return NBTEditor.contains(item, key)
    }

}