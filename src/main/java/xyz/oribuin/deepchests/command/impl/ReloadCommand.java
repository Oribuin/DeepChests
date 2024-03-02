package xyz.oribuin.deepchests.command.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandInfo;

public class ReloadCommand extends dev.rosewood.rosegarden.command.ReloadCommand {

    public ReloadCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("reload")
                .descriptionKey("command-reload-description")
                .permission("deepchests.reload")
                .build();
    }

}
