package xyz.oribuin.deepchests.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.deepchests.manager.ConfigurationManager;
import xyz.oribuin.deepchests.manager.LocaleManager;
import xyz.oribuin.deepchests.model.DeepChest;

import java.util.ArrayList;
import java.util.HashMap;

public class GiveCommand extends BaseRoseCommand {

    public GiveCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);
        ConfigurationManager configs = this.rosePlugin.getManager(ConfigurationManager.class);

        Player target = context.get("target");
        int slots = context.get("slots");
        int amount = context.get("amount");

        StringPlaceholders placeholders = StringPlaceholders.of(
                "target", target.getName(),
                "slots", slots,
                "amount", amount
        );

        // Check if the player is the owner of the warp
        if (amount < 1 || amount > 64) {
            locale.sendMessages(context.getSender(), "command-give-invalid-amount", placeholders);
            return;
        }

        // Make sure there's not negative slots
        if (slots < 1) {
            locale.sendMessages(context.getSender(), "command-give-invalid-slots", placeholders);
            return;
        }

        DeepChest chest = new DeepChest(slots, new HashMap<>());
        ItemStack item = chest.getAsItem(amount);

        target.getInventory().addItem(item);
        locale.sendMessages(context.getSender(), "command-give-success", StringPlaceholders.of("target", target.getName(), "slots", slots, "amount", amount));
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("give")
                .descriptionKey("command-give-description")
                .permission("deepchests.give")
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("target", ArgumentHandlers.PLAYER)
                .required("slots", ArgumentHandlers.INTEGER)
                .optional("amount", ArgumentHandlers.INTEGER)
                .build();
    }

}
