package me.lotiny.sara.managers

import me.despical.commons.serializer.LocationSerializer
import me.lotiny.sara.Sara
import org.bukkit.Location

class LobbyManager(private val plugin: Sara) {

    var mainLobby: Location

    init {
        mainLobby = LocationSerializer.fromString(plugin.configFile.getString("settings.main-lobby-location"))
    }
}