package xyz.oribuin.upgradeablechests.hook

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.oribuin.upgradeablechests.UpgradeableChests

object PAPI {

    /**
     * Send a message with [PlaceholderAPI] Expansions
     *
     * @param commandSender The person who the placeholders apply to
     * @param text The text
     * @return Text with [PlaceholderAPI] Placeholders.
     */
    fun apply(commandSender: CommandSender, text: String): String {

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) return text

        return if (commandSender is Player)
            PlaceholderAPI.setPlaceholders(commandSender, text)
        else
            PlaceholderAPI.setPlaceholders(null, text)
    }

    class Expansion(private val plugin: UpgradeableChests) : PlaceholderExpansion() {

        override fun onRequest(player: OfflinePlayer, params: String): String {
            // TODO
            return params
        }

        override fun getIdentifier(): String {
            return this.plugin.description.name
        }

        override fun getAuthor(): String {
            return this.plugin.description.authors[0]
        }

        override fun getVersion(): String {
            return this.plugin.description.version
        }

        init {
            this.plugin.logger.info("Registering Placeholder Expansions...")
        }

    }

}