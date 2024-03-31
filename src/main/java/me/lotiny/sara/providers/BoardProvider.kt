package me.lotiny.sara.providers

import fr.mrmicky.fastboard.FastBoard
import me.lotiny.sara.Sara
import me.lotiny.sara.arena.Arena
import me.lotiny.sara.arena.options.ArenaState
import me.lotiny.sara.utils.CC
import me.lotiny.sara.utils.Replacement
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

class BoardProvider(private val plugin: Sara) : Thread(), Listener {

    private val boards: MutableMap<UUID, FastBoard> = ConcurrentHashMap()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        start()
    }

    override fun run() {
        while (true) {
            try {
                for (board in this.boards.values) {
                    val player = board.player

                    board.updateLines(getLines(player))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun getLines(player: Player?): MutableList<String> {
        val toReturn: MutableList<String> = ArrayList()
        val config = plugin.scoreboardFile
        val arena: Arena? = plugin.arenaManager.getArena(player)
        var lines: List<String> = ArrayList()
        if (arena != null) {
            lines = when (arena.option.state) {
                ArenaState.WAITING -> config.getStringList("scoreboard.lines.waiting")
                ArenaState.STARTING -> config.getStringList("scoreboard.lines.starting")
                ArenaState.PLAYING -> config.getStringList("scoreboard.lines.playing")
            }
        }

        for (line in lines) {
            toReturn.add(formatScoreboardLine(line, player, arena))
        }

        return toReturn
    }

    private fun formatScoreboardLine(line: String, player: Player?, arena: Arena?): String {
        val replacement = Replacement(line)

        if (player != null) {
            replacement.add("<player>", player.name)
        }

        if (arena != null) {
            replacement.add("<min_players>", arena.option.minPlayers)
            replacement.add("<max_players>", arena.option.maxPlayers)
            replacement.add("<arena>", arena.name!!)
            replacement.add("<players>", arena.players.size)

            if (arena.schedule != null) {
                when (arena.option.state) {
                    ArenaState.WAITING -> {
                        replacement.add("<need_more>", arena.option.minPlayers - arena.players.size)
                    }

                    ArenaState.STARTING -> {
                        replacement.add("<start_in>", arena.schedule!!.seconds)
                    }

                    ArenaState.PLAYING -> {
                        replacement.add("<tnt_players>", arena.tntPlayers.size)
                        replacement.add("<explode_in>", arena.option.timePerRound - arena.schedule!!.seconds)
                    }
                }
            }
        }

        return replacement.toString()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val board = FastBoard(player)

        board.updateTitle(CC.translate(plugin.scoreboardFile.getString("scoreboard.title")!!))
        this.boards[player.uniqueId] = board
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        boards.remove(player.uniqueId)?.delete()
    }
}