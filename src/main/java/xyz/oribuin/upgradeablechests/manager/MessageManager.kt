package xyz.oribuin.upgradeablechests.manager

import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import xyz.oribuin.orilibrary.manager.Manager
import xyz.oribuin.orilibrary.util.FileUtils
import xyz.oribuin.orilibrary.util.HexUtils
import xyz.oribuin.orilibrary.util.StringPlaceholders
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.hook.PAPI

class MessageManager(private val plugin: UpgradeableChests) : Manager(plugin) {

    lateinit var config: FileConfiguration

    override fun enable() {
        config = YamlConfiguration.loadConfiguration(FileUtils.createFile(this.plugin, "messages.yml"))

        // Set any values that dont exist
        for (msg in Messages.values()) {
            val key = msg.name.lowercase().replace("_", " ")

            if (config.get(key) == null) config.set(key, msg.value)
        }
    }

    /**
     * Send a configuration message without any placeholders
     *
     * @param receiver  The CommandSender who receives the message.
     * @param messageId The message path
     */
    fun send(receiver: CommandSender, messageId: String) {
        this.send(receiver, messageId, StringPlaceholders.empty())
    }

    /**
     * Send a configuration messageId with placeholders.
     *
     * @param receiver     The CommandSender who receives the messageId.
     * @param messageId    The messageId path
     * @param placeholders The Placeholders
     */
    fun send(receiver: CommandSender, messageId: String, placeholders: StringPlaceholders) {
        val msg = this.config.getString(messageId)

        if (msg == null) {
            receiver.sendMessage(HexUtils.colorify("&c&lError &8| &fThis is an invalid message in the messages file, Please contact the server owner about this issue. (Id: $messageId)"))
            return
        }

        receiver.sendMessage(HexUtils.colorify(PAPI.apply(receiver, placeholders.apply(msg))))
    }

    /**
     * Send a raw message to the receiver without any placeholders
     *
     *
     * Use this to send a message to a player without the message being defined in a config.
     *
     * @param receiver The message receiver
     * @param message  The raw message
     */
    fun sendRaw(receiver: CommandSender, message: String) {
        this.sendRaw(receiver, message, StringPlaceholders.empty())
    }

    /**
     * Send a raw message to the receiver with placeholders.
     *
     *
     * Use this to send a message to a player without the message being defined in a config.
     *
     * @param receiver     The message receiver
     * @param message      The message
     * @param placeholders Message Placeholders.
     */
    fun sendRaw(receiver: CommandSender, message: String, placeholders: StringPlaceholders) {
        receiver.sendMessage(HexUtils.colorify(PAPI.apply(receiver, placeholders.apply(message))))
    }

    override fun disable() {}

    enum class Messages(val value: String) {
        PREFIX("#ff2115UpgradeableChests &8| &f"),
        CHEST_ISNT_EMPTY("You cannot destroy a chest that isnt empty!"),
        RELOAD("You have reloaded UpgradeableChests!"),

        DISABLED_WORLD("&cYou cannot do this in this world."),
        NO_PERM("&cYou do not have permission to execute this command."),
        INVALID_PLAYER("&cPlease enter a valid player."),
        INVALID_ARGUMENTS("&cPlease provide valid arguments. Correct usage: %usage%"),
        INVALID_FUNDS("&cYou do not have enough funds to do this, You need $%price%."),
        UNKNOWN_COMMAND("&cPlease include a valid command."),
        PLAYER_ONLY("&cOnly a player can execute this command."),
        CONSOLE_ONLY("&cOnly console can execute this command.");
    }
}