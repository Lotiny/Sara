package me.lotiny.sara.arena.options

import me.lotiny.sara.Sara
import me.lotiny.sara.arena.Arena

class ArenaOption(plugin: Sara, arena: Arena) {

    var minPlayers: Int
    var maxPlayers: Int
    var timePerRound: Int

    var state: ArenaState

    var round: Int

    init {
        minPlayers = plugin.arenaFile.getInt("arenas.${arena.id}.min-players")
        maxPlayers = plugin.arenaFile.getInt("arenas.${arena.id}.max-players")
        timePerRound = plugin.configFile.getInt("settings.time-per-round")

        state = ArenaState.WAITING

        round = 0
    }

}