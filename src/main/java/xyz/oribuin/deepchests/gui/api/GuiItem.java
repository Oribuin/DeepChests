package xyz.oribuin.deepchests.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.deepchests.util.ItemBuilder;

import java.util.function.Consumer;

public record GuiItem(ItemStack item, Consumer<InventoryClickEvent> function) {

    /**
     * Create a new GuiItem
     *
     * @param item The item to create
     */
    public GuiItem(ItemStack item) {
        this(item, event -> {
            event.setResult(InventoryClickEvent.Result.DENY);
            event.setCancelled(true);
        });
    }

    /**
     * Create a new GuiItem
     *
     * @param item The item to create
     */
    public static GuiItem from(ItemStack item) {
        return new GuiItem(item);
    }

    /**
     * Create a new GuiItem
     *
     * @param builder The item to create
     */
    public static GuiItem from(ItemBuilder builder) {
        return new GuiItem(builder.build());
    }


    /**
     * Create a new GuiItem
     *
     * @param item     The item to create
     * @param function The function to run when the item is clicked
     */
    public static GuiItem from(ItemStack item, Consumer<InventoryClickEvent> function) {
        return new GuiItem(item, function);
    }

}
