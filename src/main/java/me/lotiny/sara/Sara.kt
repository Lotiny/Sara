package me.lotiny.sara

import me.despical.commons.configuration.ConfigUtils
import me.lotiny.sara.arena.ArenaManager
import me.lotiny.sara.commands.handlers.CommandHandler
import me.lotiny.sara.listeners.GameListener
import me.lotiny.sara.listeners.PlayerListener
import me.lotiny.sara.listeners.WorldListener
import me.lotiny.sara.managers.LobbyManager
import me.lotiny.sara.providers.BoardProvider
import net.byteflux.libby.BukkitLibraryManager
import net.byteflux.libby.Library
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class Sara : JavaPlugin() {

    companion object {
        lateinit var instance: Sara
    }

    lateinit var configFile: FileConfiguration
    lateinit var arenaFile: FileConfiguration
    lateinit var scoreboardFile: FileConfiguration

    lateinit var arenaManager: ArenaManager
    lateinit var lobbyManager: LobbyManager

    override fun onEnable() {
        instance = this

        loadDependencies()

        this.configFile = ConfigUtils.getConfig(this, "config")
        this.arenaFile = ConfigUtils.getConfig(this, "arena")
        this.scoreboardFile = ConfigUtils.getConfig(this, "scoreboard")

        this.arenaManager = ArenaManager(this)
        this.lobbyManager = LobbyManager(this)

        Bukkit.getPluginManager().registerEvents(GameListener(), this)
        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)
        Bukkit.getPluginManager().registerEvents(WorldListener(), this)

        CommandHandler(this)
        BoardProvider(this)
    }

    override fun onDisable() {
        arenaManager.saveArenas()
    }

    private fun loadDependencies() {
        val libraryManager = BukkitLibraryManager(this)
        libraryManager.addJitPack()
        libraryManager.addMavenCentral()

        arrayOf(
            Library.builder().groupId("com{}github{}Despical").artifactId("Commons").version("1.7.1").build(),
            Library.builder().groupId("fr{}mrmicky").artifactId("fastboard").version("2.1.0").build(),
            Library.builder().groupId("com{}github{}Revxrsal{}Lamp").artifactId("common").version("3.2.0").build(),
            Library.builder().groupId("com{}github{}Revxrsal{}Lamp").artifactId("bukkit").version("3.2.0").build()
        ).forEach {
            libraryManager.loadLibrary(it)
        }
    }
}
