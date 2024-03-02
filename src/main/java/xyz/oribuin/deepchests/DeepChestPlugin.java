package xyz.oribuin.deepchests;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import xyz.oribuin.deepchests.gui.api.GuiListener;
import xyz.oribuin.deepchests.listener.PlayerListeners;
import xyz.oribuin.deepchests.manager.CommandManager;
import xyz.oribuin.deepchests.manager.ConfigurationManager;
import xyz.oribuin.deepchests.manager.LocaleManager;

import java.util.List;

public class DeepChestPlugin extends RosePlugin {

    private static DeepChestPlugin instance;

    public DeepChestPlugin() {
        super(
                -1, // The resource id of the plugin
                -1, // The project id of the plugin
                ConfigurationManager.class, // The configuration manager
                null,
                LocaleManager.class, // The locale manager
                CommandManager.class // The command manager
        );

        instance = this;
    }

    @Override
    public void enable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(this), this);
        pluginManager.registerEvents(new GuiListener(), this);
    }

    @Override
    public void reload() {
        super.reload();
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void disable() {
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of();
    }

    public static DeepChestPlugin get() {
        return instance;
    }

}
