package xyz.oribuin.upgradeablechests.util

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
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
}