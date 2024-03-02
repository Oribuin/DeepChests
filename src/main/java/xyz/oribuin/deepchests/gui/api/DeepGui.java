package xyz.oribuin.deepchests.gui.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DeepGui implements InventoryHolder {

    private final Map<Integer, GuiItem> menuItems;
    private final Inventory inventory;
    private int size;
    private String title;
    private Consumer<InventoryClickEvent> defaultClickAction;
    private Consumer<InventoryCloseEvent> closeAction;

    public DeepGui(int size, String title) {
        if (size % 9 != 0) {
            throw new IllegalArgumentException("Size must be a multiple of 9");
        }

        this.menuItems = new HashMap<>();
        this.size = size;
        this.title = title;
        this.inventory = Bukkit.createInventory(this, size, title);
        this.defaultClickAction = null;
        this.closeAction = null;
    }

    /**
     * Open the gui for a player
     *
     * @param player The player to open the gui for
     */
    public void open(Player player) {
        if (player.isSleeping()) return;

        player.openInventory(this.inventory);
    }

    /**
     * Set an item in the gui that will never change
     *
     * @param slot The slot to set the item
     * @param item The item to set
     */
    public void setPerm(int slot, GuiItem item) {
        this.menuItems.put(slot, item);
        this.inventory.setItem(slot, item.item().clone());
    }

    /**
     * Set an item in the gui that will never change
     *
     * @param slots The slots to set the item
     * @param item  The item to set
     */
    public void setPerm(List<Integer> slots, GuiItem item) {
        slots.forEach(slot -> this.setPerm(slot, item));
    }

    /**
     * Remove a permanent item from the gui
     *
     * @param slot The slot to remove the item
     */
    public void removePerm(int slot) {
        this.menuItems.remove(slot);
        this.inventory.setItem(slot, null);
    }

    /**
     * Add a temporary item to the gui
     *
     * @param slot The slot to add the item
     * @param item The item to add
     */
    public void setTemp(int slot, ItemStack item) {
        this.inventory.setItem(slot, item.clone());
    }

    /**
     * Check if a slot is a permanent item
     *
     * @param slot The slot to check
     * @return true if the slot is a permanent item
     */
    public boolean isPerm(int slot) {
        return this.menuItems.containsKey(slot);
    }

    /**
     * Set a function whenever a player clicks an item
     *
     * @param eventConsumer The function to run
     * @return The current gui
     */
    public DeepGui setDefaultClick(Consumer<InventoryClickEvent> eventConsumer) {
        this.defaultClickAction = eventConsumer;
        return this;
    }

    /**
     * Set a function whenever a player closes the inventory
     *
     * @param eventConsumer The function to run
     * @return The current gui
     */
    public DeepGui setCloseAction(Consumer<InventoryCloseEvent> eventConsumer) {
        this.closeAction = eventConsumer;
        return this;
    }

    /**
     * Get the item in the gui
     *
     * @param slot The slot to get the item
     * @return The item in the slot
     */
    public GuiItem getGuiItem(int slot) {
        return this.menuItems.get(slot);
    }

    /**
     * Get all the menu items in the gui
     *
     * @return The menu items
     */
    public Map<Integer, GuiItem> getMenuItems() {
        return menuItems;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Consumer<InventoryClickEvent> getDefaultClickAction() {
        return defaultClickAction;
    }

    public Consumer<InventoryCloseEvent> getCloseAction() {
        return closeAction;
    }

}
