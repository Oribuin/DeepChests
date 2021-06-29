package xyz.oribuin.upgradeablechests.obj;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Chest {

    private final int id;
    private int tier;
    private final Location location;
    private List<ItemStack> items;

    public Chest(int id, int tier, Location location) {
        this.id = id;
        this.tier = tier;
        this.location = location;
        this.items = Collections.emptyList();
    }

    public int getId() {
        return id;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public Location getLocation() {
        return location;
    }
}
