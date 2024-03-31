package me.lotiny.sara.commands.handlers

import me.lotiny.sara.Sara
import me.lotiny.sara.commands.ArenaCommand
import me.lotiny.sara.commands.JoinCommand
import me.lotiny.sara.commands.LeaveCommand
import org.bukkit.entity.Player
import revxrsal.commands.autocomplete.SuggestionProviderFactory
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.ExecutableCommand


class CommandHandler(private val plugin: Sara) {

    private val commandHandler: BukkitCommandHandler = BukkitCommandHandler.create(plugin)

    init {
        commandHandler.registerDependency(Sara.javaClass, plugin)
        commandHandler.setExceptionHandler(CommandExceptionHandler())

        commandHandler.autoCompleter.registerSuggestionFactory(0, SuggestionProviderFactory.forType(Player::class.java) { _: List<String?>?, _: CommandActor?, _: ExecutableCommand? ->
            val list: MutableList<String> = ArrayList()

            for (player in plugin.server.onlinePlayers) {
                list.add(player.name)
            }

            list
        })

        register()
    }

    private fun register() {
        arrayOf(
            ArenaCommand(),
            JoinCommand(),
            LeaveCommand()
        ).forEach {
            commandHandler.register(it)
        }
    }
}