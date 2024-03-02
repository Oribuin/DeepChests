package xyz.oribuin.deepchests.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import xyz.oribuin.deepchests.command.impl.GiveCommand;
import xyz.oribuin.deepchests.command.impl.HelpCommand;
import xyz.oribuin.deepchests.command.impl.ReloadCommand;

public class DeepChestCommand extends BaseRoseCommand {

    public DeepChestCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        // Unused
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("deepchests")
                .descriptionKey("command-base-description")
                .build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .requiredSub("command", new HelpCommand(this.rosePlugin, this),
                        new ReloadCommand(this.rosePlugin),
                        new GiveCommand(this.rosePlugin)
                );
    }
}
