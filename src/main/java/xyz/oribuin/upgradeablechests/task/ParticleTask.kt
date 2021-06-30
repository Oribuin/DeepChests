package xyz.oribuin.upgradeablechests.task

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import xyz.oribuin.upgradeablechests.UpgradeableChests
import xyz.oribuin.upgradeablechests.manager.DataManager

class ParticleTask(private val plugin: UpgradeableChests) : BukkitRunnable() {

    init {
        this.runTaskTimerAsynchronously(plugin, 0, 3)
    }

    override fun run() {
        this.plugin.getManager(DataManager::class.java).cachedChests.forEach { entry ->
            val loc = entry.value.location

            for (i in 0..5) loc.world?.spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 3, 0.3, 0.3, 0.3,
                Particle.DustTransition(Color.fromRGB(188, 79, 156), Color.fromRGB(248, 7, 89), 1f))
        }
    }

}