package xyz.oribuin.deepchests.gui.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {

    /**
     * Run when a player clicks in a gui
     *
     * @param event The event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof DeepGui gui)) return;

        // If the default click action is not null, run it.
        if (gui.getDefaultClickAction() != null) {
            gui.getDefaultClickAction().accept(event);
        }

        // If the slot is not in the menuItems, return.
        GuiItem item = gui.getGuiItem(event.getSlot());
        if (item != null && item.function() != null) {
            item.function().accept(event);
        }
    }

    /**
     * Run when a player closes a gui
     *
     * @param event The event
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof DeepGui gui)) return;

        if (gui.getCloseAction() != null) {
            gui.getCloseAction().accept(event);
        }
    }

}
