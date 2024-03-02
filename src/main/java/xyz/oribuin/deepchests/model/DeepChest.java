package xyz.oribuin.deepchests.model;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.oribuin.deepchests.DeepChestPlugin;
import xyz.oribuin.deepchests.manager.ConfigurationManager;
import xyz.oribuin.deepchests.util.ChestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A record to store the data of a deep chest
 *
 * @param totalSlots The total slots of the chest
 * @param contents   The contents of the chest
 */
@SuppressWarnings("unused")
public record DeepChest(int totalSlots, Map<Integer, ItemStack> contents) {

    /**
     * Deposit an item into the chest
     *
     * @param item The item to deposit
     */
    public void deposit(ItemStack item) {
        if (this.contents.size() >= this.totalSlots) return;

        for (int i = 0; i < this.totalSlots; i++) {
            ItemStack slot = this.contents.get(i);
            if (slot == null || slot.getType().isAir()) {
                this.contents.put(i, item);
                break;
            }

            if (!slot.isSimilar(item)) continue;

            int maxStackSize = item.getMaxStackSize();
            int currentStackSize = slot.getAmount();

            if (currentStackSize + item.getAmount() <= maxStackSize) {
                slot.setAmount(currentStackSize + item.getAmount());
                this.contents.put(i, slot);
                return;
            }

            int remaining = maxStackSize - currentStackSize;
            if (remaining == 0) break;

            item.setAmount(item.getAmount() - remaining);
            slot.setAmount(maxStackSize);
            this.contents.put(i, slot);
        }
    }

    /**
     * Withdraw an item from the chest
     *
     * @param item The item to withdraw
     */
    public void withdraw(ItemStack item) {
        this.contents.remove(item);
    }

    /**
     * Overwrite the total slots of the chest and return a new DeepChest
     *
     * @param totalSlots The new total slots
     * @return The new DeepChest
     */
    public DeepChest totalSlots(int totalSlots) {
        return new DeepChest(totalSlots, this.contents);
    }

    /**
     * Save the DeepChest to a PersistentDataContainer
     *
     * @param container The container to save the data to.
     */
    public void save(PersistentDataContainer container) {
        container.set(Keys.SLOTS, DataType.INTEGER, this.totalSlots);
        container.set(Keys.CONTENTS,
                DataType.asMap(DataType.INTEGER, DataType.ITEM_STACK),
                this.contents
        );
    }

    /**
     * Create a new DeepChest from a PersistentDataContainer
     *
     * @param container The container to create the DeepChest from.
     * @return The new DeepChest
     */
    public static DeepChest from(PersistentDataContainer container) {
        Integer slots = container.get(Keys.SLOTS, PersistentDataType.INTEGER);
        if (slots == null) return null;

        Map<Integer, ItemStack> contents = container.getOrDefault(
                Keys.CONTENTS,
                DataType.asMap(DataType.INTEGER, DataType.ITEM_STACK),
                new HashMap<>()
        );

        return new DeepChest(slots, contents);
    }

    /**
     * Get the DeepChest as an ItemStack
     *
     * @return The ItemStack
     */
    public ItemStack getAsItem(int amount) {
        ConfigurationManager manager = DeepChestPlugin.get().getManager(ConfigurationManager.class);
        ItemStack item = ChestUtils.deserialize(manager.getConfig(), null, "chest-item", StringPlaceholders.of("slots", this.totalSlots));
        // Check if the item is null
        if (item == null) return null;

        // Item should have meta but just in case
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        this.save(meta.getPersistentDataContainer());
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }

    /**
     * A record to store the keys for the DeepChest
     */
    private static class Keys {
        public static final NamespacedKey SLOTS = new NamespacedKey(DeepChestPlugin.get(), "slots");
        public static final NamespacedKey CONTENTS = new NamespacedKey(DeepChestPlugin.get(), "contents");
    }

}
