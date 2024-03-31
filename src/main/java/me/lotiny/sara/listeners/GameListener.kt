package me.lotiny.sara.listeners

import me.lotiny.sara.Sara
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class GameListener : Listener {

    private val plugin: Sara = Sara.instance

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player

            if (plugin.arenaManager.getArena(player) == null) {
                event.isCancelled = true
            } else {
                event.damage = 0.0
            }
        }
    }

    @EventHandler
    fun onPlayerTag(event: EntityDamageByEntityEvent) {
        if (event.entity is Player && event.damager is Player) {
            val player = event.entity as Player
            val damager = event.damager as Player
            val arena = plugin.arenaManager.getArena(player) ?: return

            if (arena.isTNTPlayer(damager) && !arena.isTNTPlayer(player)) {
                arena.setIsIT(player)
                arena.removeIsIT(damager)
                player.world.spawnEntity(player.location, EntityType.FIREWORK)
            }
        }
    }
}