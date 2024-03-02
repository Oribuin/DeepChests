package xyz.oribuin.deepchests.listener;

import dev.rosewood.rosegarden.RosePlugin;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import xyz.oribuin.deepchests.gui.StorageGUI;
import xyz.oribuin.deepchests.model.DeepChest;

public class PlayerListeners implements Listener {

    private final RosePlugin plugin;

    public PlayerListeners(RosePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Overwrite the inventory opening to open the storage GUI
     *
     * @param event The event
     */
    @EventHandler(ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        Location invLocation = event.getInventory().getLocation();
        if (invLocation == null) return;

        if (!(invLocation.getBlock().getState() instanceof Container containerBlock))
            return;

        DeepChest chest = DeepChest.from(containerBlock.getPersistentDataContainer());
        if (chest == null) return;

        // Modify the chest
        event.setCancelled(true);
        StorageGUI.open((Player) event.getPlayer(), invLocation.getBlock(), chest, 1);
    }

    /**
     * Overwrite the block placing to save the chest
     *
     * @param event The event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();

        if (!(block.getState() instanceof Container containerBlock))
            return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        DeepChest chest = DeepChest.from(meta.getPersistentDataContainer());
        if (chest == null) return;

        // Modify the chest
        PersistentDataContainer container = containerBlock.getPersistentDataContainer();
        chest.save(container);
        containerBlock.update(true);
    }

    /**
     * Overwrite the block breaking to drop the chest
     *
     * @param event The event
     */
    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Container containerBlock))
            return;

        DeepChest chest = DeepChest.from(containerBlock.getPersistentDataContainer());
        if (chest == null) return;

        event.setDropItems(false);

        ItemStack item = chest.getAsItem(1);
        if (item == null) return;

        // Drop the chest
        block.getWorld().dropItemNaturally(block.getLocation(), item);
    }

}
