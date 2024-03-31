package me.lotiny.sara.commands

import me.lotiny.sara.Sara
import me.lotiny.sara.arena.Arena
import me.lotiny.sara.utils.CC
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.DefaultFor
import revxrsal.commands.annotation.Dependency
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.annotation.Usage
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("arena") @CommandPermission("sara.command.arena")
class ArenaCommand {

    @Dependency
    private lateinit var plugin: Sara

    @DefaultFor("arena")
    fun getHelp(player: Player) {

    }

    @Subcommand("create") @Usage("arena create <id>")
    fun createArena(player: Player, id: String) {
        if (plugin.arenaManager.arenas.containsKey(id)) {
            player.sendMessage(CC.translate("&cThis arena id is already exist."))
            return
        }

        val arena = Arena(id)
        arena.name = id
        arena.option.minPlayers = 8
        arena.option.maxPlayers = 32
        plugin.arenaManager.arenas[id] = arena
        player.sendMessage(CC.translate("&aArena `${id}` created!"))
        player.sendMessage(CC.translate("&c&lREMINDER&c don't forget to set location."))
    }

    @Subcommand("setlocation") @Usage("arena setlocation <id>")
    fun setLocationArena(player: Player, id: String) {
        val arena = plugin.arenaManager.getArena(id)
        if (arena == null) {
            player.sendMessage(CC.translate("&cThe arena with this id doesn't exist."))
            return
        }

        arena.location = player.location
        player.sendMessage(CC.translate("&aSuccessfully set your location to `${id}` arena location."))
    }

    @Subcommand("setmaxplayer") @Usage("arena setmaxplayer <id> <amount>")
    fun setMaxPlayerArena(player: Player, id: String, amount: Int) {
        val arena = plugin.arenaManager.getArena(id)
        if (arena == null) {
            player.sendMessage(CC.translate("&cThe arena with this id doesn't exist."))
            return
        }

        arena.option.maxPlayers = amount
        player.sendMessage(CC.translate("&aSuccessfully set max player for `${id}` arena to $amount players."))
    }

    @Subcommand("setminplayer") @Usage("arena setminplayer <id> <amount>")
    fun setMinPlayerArena(player: Player, id: String, amount: Int) {
        val arena = plugin.arenaManager.getArena(id)
        if (arena == null) {
            player.sendMessage(CC.translate("&cThe arena with this id doesn't exist."))
            return
        }

        arena.option.minPlayers = amount
        player.sendMessage(CC.translate("&aSuccessfully set min player for `${id}` arena to $amount players."))
    }
}