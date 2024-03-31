package me.lotiny.sara.arena.schedules

import me.lotiny.sara.Sara
import me.lotiny.sara.arena.Arena
import me.lotiny.sara.arena.options.ArenaState
import me.lotiny.sara.utils.Utilities
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.scheduler.BukkitRunnable

class ArenaSchedule(private val plugin: Sara, private val arena: Arena) : BukkitRunnable() {

    private var ended = false

    var seconds = 0

    init {
        runTaskTimerAsynchronously(plugin, 20L, 20L)
    }

    override fun run() {
        if (ended) {
            seconds--

            when (seconds) {
                in 1..5, 10, 30 -> {
                    arena.broadcast("&eThe game will ended in &6${seconds}&e ${if (seconds == 1) "second" else "seconds"}.")
                }
            }

            if (seconds == 0) {
                arena.players.forEach {
                    it.teleport(plugin.lobbyManager.mainLobby)
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        Utilities.resetPlayer(it, GameMode.SURVIVAL)
                    })
                }
                arena.players.clear()

                arena.spectators.forEach {
                    it.teleport(plugin.lobbyManager.mainLobby)
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        Utilities.resetPlayer(it, GameMode.SURVIVAL)
                    })
                }
                arena.spectators.clear()
                arena.schedule = null
                arena.option.state = ArenaState.WAITING
                cancel()
            }
            return
        }

        when (arena.option.state) {
            ArenaState.WAITING -> {
                if (arena.players.size >= arena.option.minPlayers) {
                    seconds = 60
                    arena.option.state = ArenaState.STARTING
                }

                if (arena.players.size == 0) {
                    arena.schedule = null
                    cancel()
                }
            }

            ArenaState.STARTING -> {
                seconds--

                when (seconds) {
                    in 1..5, 10, 30, 60 -> {
                        arena.broadcast("&eThe game will begin in &6${seconds}&e ${if (seconds == 1) "second" else "seconds"}.")
                    }
                }

                if (seconds == 0) {
                    if (arena.players.size < arena.option.minPlayers) {
                        arena.option.state = ArenaState.WAITING
                        arena.broadcast("&cThe game has been cancelled because not enough players.")
                        return
                    }

                    arena.teleportPlayerToCenter()
                    arena.option.state = ArenaState.PLAYING

                    plugin.arenaManager.startNewRound(arena)
                }
            }

            ArenaState.PLAYING -> {
                seconds++

                if (seconds == arena.option.timePerRound) {
                    plugin.arenaManager.executeTNT(arena)
                    if (arena.players.size == 1) {
                        arena.broadcast("&eWinner:&6&l ${arena.players[0].name}")
                        ended = true
                        seconds = 30
                        return
                    }

                    plugin.arenaManager.startNewRound(arena)

                    seconds = 0
                }
            }
        }
    }
}