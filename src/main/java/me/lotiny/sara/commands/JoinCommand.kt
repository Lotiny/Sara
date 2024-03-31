package me.lotiny.sara.commands

import me.lotiny.sara.Sara
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Dependency
import revxrsal.commands.annotation.Usage

class JoinCommand {

    @Dependency
    private lateinit var plugin: Sara

    @Command("join") @Usage("join <id>")
    fun execute(player: Player, id: String) {
        plugin.arenaManager.joinArena(player, id)
    }
}