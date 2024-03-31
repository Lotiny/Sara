package me.lotiny.sara.utils

import org.bukkit.GameMode
import org.bukkit.entity.Player

object Utilities {

    fun resetPlayer(player: Player, gameMode: GameMode) {
        player.allowFlight = false
        player.isFlying = false
        player.gameMode = gameMode
        player.maxHealth = 20.0
        player.health = 20.0
        player.foodLevel = 20
        player.inventory.heldItemSlot = 0
        player.inventory.clear()
        player.inventory.setArmorContents(null)
        player.level = 0
        player.totalExperience = 0
        player.exp = 0.0F

        player.activePotionEffects.forEach {
            potionEffect -> player.removePotionEffect(potionEffect.type)
        }
    }
}