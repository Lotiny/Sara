package me.lotiny.sara.commands

import me.lotiny.sara.Sara
import me.lotiny.sara.arena.options.ArenaState
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Dependency

class LeaveCommand {

    @Dependency
    private lateinit var plugin: Sara

    @Command("leave")
    fun execute(player: Player) {
        val arena = plugin.arenaManager.getArena(player)
        if (arena == null) {
            player.sendMessage("&cYou aren't in any arena.")
            return
        }

        player.teleport(plugin.lobbyManager.mainLobby)
        if (arena.option.state == ArenaState.PLAYING) {
            plugin.arenaManager.eliminate(player, true)
        } else {
            arena.players.remove(player)
            arena.broadcast("&6${player.name}&e Left! &7(${arena.players.size}/${arena.option.maxPlayers})")
            player.activePotionEffects.forEach {
                player.removePotionEffect(it.type)
            }
        }
    }
}