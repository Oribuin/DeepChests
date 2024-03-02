package xyz.oribuin.deepchests.gui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.deepchests.gui.api.DeepGui;
import xyz.oribuin.deepchests.gui.api.GuiItem;
import xyz.oribuin.deepchests.model.DeepChest;
import xyz.oribuin.deepchests.util.ItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Fix pagination
 * TODO: BUG TEST PLEASE NO D UPE BUGS
 * TODO: Make sure the chest cannot be interacted with while its open
 */
public class StorageGUI {

    private static final List<Location> openedChests = new ArrayList<>();

    /**
     * Open the storage GUI for the player
     *
     * @param who   The player to open the GUI for
     * @param block The location of the chest
     * @param chest The chest to open
     * @param page  The page to open
     */
    public static void open(Player who, Block block, DeepChest chest, int page) {
        DeepGui gui = new DeepGui(54, "Deep Chest");
        int totalPages = (int) Math.ceil((double) chest.contents().size() / 45);
        if (page > totalPages) page = totalPages;

        // Add the items to the GUI
        int start = Math.max(0, (page - 1) * 45);
        int end = Math.min(start + 45, chest.totalSlots());

        int finalPage = page;
        gui.setCloseAction(event -> {
            if (!(block.getState() instanceof Container container)) return;
            // Change slots start-end with new content
            Map<Integer, ItemStack> current = new HashMap<>(chest.contents());
            Map<Integer, ItemStack> menuContents = new HashMap<>();
            Inventory inventory = event.getInventory();

            ItemStack air = new ItemStack(Material.AIR);
            for (int i = 0; i < inventory.getSize(); i++) {
                if (gui.getGuiItem(i) != null)
                    continue;

                ItemStack item = inventory.getItem(i);
                menuContents.put(i, item == null ? air : item);
            }

            // Readjust the slots
            int pageStart = Math.max(0, (finalPage - 1) * 45);
            menuContents.forEach((key, item) -> {
                int slot = key + pageStart;
                if (item == null || item.getType() == Material.AIR) {
                    current.remove(slot);
                    return;
                }

                current.put(slot, item);
            });

            DeepChest newChest = new DeepChest(chest.totalSlots(), current);
            newChest.save(container.getPersistentDataContainer());
            container.update(true);
        });

        // Add items to the GUI
        gui.setPerm(bottomRow(), GuiItem.from(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ")));

        gui.setPerm(45, GuiItem.from(new ItemBuilder(Material.ARROW).name("Previous Page").build(), event -> {
            immovable(event);
            open(who, block, chest, finalPage - 1);
        }));

        gui.setPerm(53, GuiItem.from(new ItemBuilder(Material.ARROW).name("Next Page").build(), event -> {
            immovable(event);
            open(who, block, chest, finalPage + 1);
        }));

        chest.contents().forEach((slot, item) -> {
            if (slot < start || slot >= end) {
                return;
            }

            gui.setTemp(slot - start, item);

        });

        gui.open(who);
    }

    /**
     * @return The bottom row of the inventory
     */
    private static List<Integer> bottomRow() {
        List<Integer> slots = new ArrayList<>();
        for (int i = 45; i < 53; i++) {
            slots.add(i);
        }
        return slots;
    }

    /**
     * Cancel the event and deny the result
     *
     * @param event The event to cancel
     */
    private static void immovable(InventoryClickEvent event) {
        event.setResult(InventoryClickEvent.Result.DENY);
        event.setCancelled(true);
    }

}
