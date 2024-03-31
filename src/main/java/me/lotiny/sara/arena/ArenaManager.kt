package me.lotiny.sara.arena

import me.despical.commons.configuration.ConfigUtils
import me.despical.commons.serializer.LocationSerializer
import me.lotiny.sara.Sara
import me.lotiny.sara.arena.options.ArenaState
import me.lotiny.sara.arena.schedules.ArenaSchedule
import me.lotiny.sara.utils.CC
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.concurrent.ConcurrentHashMap

class ArenaManager(private val plugin: Sara) {

    val arenas: MutableMap<String, Arena> = ConcurrentHashMap()

    init {
        loadArenas()
    }

    private fun loadArenas() {
        val section = plugin.arenaFile.getConfigurationSection("arenas")
        if (section == null) {
            plugin.logger.warning("Failed to load arena please check in `arena.yml`.")
            return
        }

        for (key in section.getKeys(false)) {
            val arena = Arena(key)
            arena.name = section.getString("${key}.name")
            arena.location = LocationSerializer.fromString(section.getString("${key}.location"))

            this.arenas[key] = arena
        }
    }

    fun saveArenas() {
        arenas.forEach { (id, arena) ->
            plugin.arenaFile.set("arenas.${id}.name", arena.name)
            plugin.arenaFile.set("arenas.${id}.location", LocationSerializer.toString(arena.location))
            plugin.arenaFile.set("arenas.${id}.min-players", arena.option.minPlayers)
            plugin.arenaFile.set("arenas.${id}.max-players", arena.option.maxPlayers)
        }

        ConfigUtils.saveConfig(plugin, plugin.arenaFile, "arena")
        plugin.logger.info("Saved all arena to `arena.yml`.")
    }

    fun getArena(player: Player?): Arena? {
        if (player == null) {
            return null
        }

        return arenas.values.firstOrNull {
            it.players.contains(player)
        }
    }

    fun getArena(id: String): Arena? {
        if (!arenas.containsKey(id)) {
            return null
        }

        return arenas[id]
    }

    fun joinArena(player: Player, id: String) {
        val arena = getArena(id)
        if (arena == null) {
            player.sendMessage(CC.translate("&cFailed to find this arena."))
            return
        }

        player.teleport(arena.location!!)
        Bukkit.getOnlinePlayers().forEach {
            it.hidePlayer(player)
            player.hidePlayer(it)
        }

        if (arena.option.state == ArenaState.PLAYING) {
            setSpectator(player, arena)
        } else {
            arena.players.add(player)
            arena.players.forEach {
                it.showPlayer(player)
                player.showPlayer(player)
            }

            if (arena.schedule == null) {
                arena.schedule = ArenaSchedule(plugin, arena)
            }

            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, false, false))
            arena.broadcast("&6${player.name}&e joined! &7(${arena.players.size}/${arena.option.maxPlayers})")
        }
    }

    fun startNewRound(arena: Arena) {
        if (arena.players.size <= 5) {
            arena.teleportPlayerToCenter()
        }

        arena.option.round += 1
        choseTNTPlayers(arena).forEach {
            arena.setIsIT(it)
        }
    }

    private fun choseTNTPlayers(arena: Arena): MutableSet<Player> {
        arena.players.shuffle()

        val set: MutableSet<Player> = HashSet()
        if (arena.players.size <= 5) {
            set.add(arena.players[0])
        } else {
            var player: Int = (arena.players.size * 0.3).toInt()
            if (player <= 0) {
                player = 1
            }

            for (i in 0 until player) {
                set.add(arena.players[i])
            }
        }

        return set
    }

    fun executeTNT(arena: Arena) {
        arena.tntPlayers.forEach {
            eliminate(it, false)
        }
    }

    fun eliminate(player: Player, isQuit: Boolean) {
        val arena = getArena(player) ?: return

        if (arena.isTNTPlayer(player)) {
            arena.playTNTSound()
            arena.tntPlayers.remove(player)
            arena.players.remove(player)

            if (!isQuit) {
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    setSpectator(player, arena)
                })
            }
        }
    }

    private fun setSpectator(player: Player, arena: Arena) {
        arena.spectators.add(player)
        // Make player invisible
        arena.players.forEach {
            it.hidePlayer(player)
        }
        arena.spectators.forEach {
            it.showPlayer(player)
            player.showPlayer(it)
        }
        // Remove potion effect
        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }

        player.allowFlight = true
        player.isFlying = true
    }
}