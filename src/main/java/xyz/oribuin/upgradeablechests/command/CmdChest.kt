package xyz.oribuin.upgradeablechests.command

import org.bukkit.command.CommandSender
import xyz.oribuin.orilibrary.command.Command
import xyz.oribuin.upgradeablechests.UpgradeableChests

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
        TODO("Not finished command yet.")
    }

}