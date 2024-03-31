package me.lotiny.sara.commands.handlers

import me.lotiny.sara.utils.CC
import revxrsal.commands.bukkit.exception.BukkitExceptionAdapter
import revxrsal.commands.bukkit.exception.InvalidPlayerException
import revxrsal.commands.command.CommandActor
import revxrsal.commands.exception.MissingArgumentException
import revxrsal.commands.exception.NoPermissionException

class CommandExceptionHandler : BukkitExceptionAdapter() {

    override fun missingArgument(actor: CommandActor, exception: MissingArgumentException) {
        actor.error(CC.translate("&cUsage: /" + exception.command.usage))
    }

    override fun noPermission(actor: CommandActor, exception: NoPermissionException) {
        actor.error(CC.translate("&cYou do not have permissions to execute this command."))
    }

    override fun invalidPlayer(actor: CommandActor, exception: InvalidPlayerException) {
        actor.error(CC.translate("&cThe player you specified is not online."))
    }
}