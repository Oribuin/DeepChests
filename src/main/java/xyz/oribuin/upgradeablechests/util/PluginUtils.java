package xyz.oribuin.upgradeablechests.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public final class PluginUtils {

    /**
     * Deserializes a String into an ItemStack
     *
     * @param serialization The serialized {@link ItemStack}
     * @return The ItemStack that was serialized.
     */
    public static ItemStack handleDeserialization(final String serialization) {
        final YamlConfiguration configuration = new YamlConfiguration();

        try {
            configuration.loadFromString(new String(Base64.getDecoder().decode(serialization)));
        } catch (final InvalidConfigurationException exception) {
            return null;
        }

        return configuration.getItemStack("item-stack");
    }

    /**
     * Serializes the given {@link ItemStack} to a {@link String}
     *
     * @param itemStack to be serialized
     * @return A string containing the serialized contents
     */
    public static String handleSerialization(final ItemStack itemStack) {
        final YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("item-stack", itemStack);

        return Base64.getEncoder().encodeToString(configuration.saveToString().getBytes());
    }

}
