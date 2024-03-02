package xyz.oribuin.deepchests.manager;


import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import xyz.oribuin.deepchests.DeepChestPlugin;

import java.util.List;

public class ConfigurationManager extends AbstractConfigurationManager {

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{};
    }

    public enum Setting implements RoseSetting {
        CHEST_ITEM("chest-item", null, "Configure the item that will be used as the chest."),
        CHEST_MATERIAL("chest-item.material", "CHEST", "The material of the chest."),
        CHEST_NAME("chest-item.name", "<#1baeff>&lDeep Chest", "The name of the chest."),
        CHEST_LORE("chest-item.lore", List.of(
                " &7| &fPlace this chest to create a deep chest.",
                " &7| &fThis chest can store up to %slots% items!",
                " &7| ",
                " &7| &fRight click to open the chest."
        ), "The lore of the chest."),
        ;

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return DeepChestPlugin.get().getManager(ConfigurationManager.class).getConfig();
        }
    }
}
