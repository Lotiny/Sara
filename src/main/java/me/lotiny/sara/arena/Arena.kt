package me.lotiny.sara.arena

import me.despical.commons.compat.XSound
import me.lotiny.sara.Sara
import me.lotiny.sara.arena.options.ArenaOption
import me.lotiny.sara.arena.schedules.ArenaSchedule
import me.lotiny.sara.utils.CC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Arena(val id:String) {

    private val plugin: Sara = Sara.instance

    var name: String? = null
    var location: Location? = null

    var players: MutableList<Player> = ArrayList()
    var spectators: MutableList<Player> = ArrayList()
    var tntPlayers: MutableList<Player> = ArrayList()
    var option: ArenaOption = ArenaOption(plugin, this)

    var schedule: ArenaSchedule? = null

    fun isTNTPlayer(player: Player): Boolean {
        return tntPlayers.contains(player)
    }

    fun broadcast(message: String) {
        players.forEach {
            it.sendMessage(CC.translate(message))
        }

        spectators.forEach {
            it.sendMessage(CC.translate(message))
        }
    }

    fun playTNTSound() {
        players.forEach {
            XSound.ENTITY_GENERIC_EXPLODE.play(it)
        }

        spectators.forEach {
            XSound.ENTITY_GENERIC_EXPLODE.play(it)
        }
    }

    fun teleportPlayerToCenter() {
        players.forEach {
            it.teleport(location!!)
        }
    }

    fun setIsIT(player: Player) {
        tntPlayers.add(player)
        player.inventory.helmet = ItemStack(Material.TNT)
        player.inventory.setItem(0, ItemStack(Material.TNT))
        Bukkit.getScheduler().runTask(plugin, Runnable {
            player.removePotionEffect(PotionEffectType.SPEED)
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2, false, false))
        })
        broadcast("&e${player.name}&c is IT!")
    }

    fun removeIsIT(player: Player) {
        tntPlayers.remove(player)
        player.inventory.helmet = null
        player.inventory.clear()
        Bukkit.getScheduler().runTask(plugin, Runnable {
            player.removePotionEffect(PotionEffectType.SPEED)
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false))
        })
    }
}