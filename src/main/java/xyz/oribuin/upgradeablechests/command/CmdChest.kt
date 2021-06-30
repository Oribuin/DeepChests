package xyz.oribuin.upgradeablechests.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.oribuin.orilibrary.command.Command
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.manager.TierManager

@Command.Info(
    name = "upgradeablechests",
    aliases = ["upchests"],
    playerOnly = false,
    permission = "upgradeablechests.command",
    usage = "/upchests",
    description = "Main command for UpgradeableChests",
    subcommands = []
)
class CmdChest(private val plugin: UpgradeableChests) : Command(plugin) {

    override fun runFunction(sender: CommandSender, label: String, args: Array<String>) {
        if (sender is Player) {
            this.plugin.getManager(TierManager::class.java).tiers
                .forEach { (_, u) -> sender.inventory.addItem(u.displayItem) }
        }

    }

}