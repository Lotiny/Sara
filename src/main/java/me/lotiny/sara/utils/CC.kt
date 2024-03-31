package me.lotiny.sara.utils

import org.bukkit.ChatColor

object CC {

    private val MAP: MutableMap<String, ChatColor> = HashMap()

    val BLUE: String
    val AQUA: String
    val YELLOW: String
    val RED: String
    val GRAY: String
    val GOLD: String
    val GREEN: String
    val WHITE: String
    val BLACK: String
    val BOLD: String
    val ITALIC: String
    val UNDER_LINE: String
    val STRIKE_THROUGH: String
    val RESET: String
    val MAGIC: String
    val DARK_BLUE: String
    val DARK_AQUA: String
    val DARK_GRAY: String
    val DARK_GREEN: String
    val DARK_PURPLE: String
    val DARK_RED: String
    val PINK: String
    val MENU_BAR: String
    val CHAT_BAR: String
    val SB_BAR: String

    init {
        MAP["pink"] = ChatColor.LIGHT_PURPLE
        MAP["orange"] = ChatColor.GOLD
        MAP["purple"] = ChatColor.DARK_PURPLE

        for (chatColor in ChatColor.entries) {
            MAP[chatColor.name.replace("_", "").toLowerCase()] = chatColor
        }

        BLUE = ChatColor.BLUE.toString()
        AQUA = ChatColor.AQUA.toString()
        YELLOW = ChatColor.YELLOW.toString()
        RED = ChatColor.RED.toString()
        GRAY = ChatColor.GRAY.toString()
        GOLD = ChatColor.GOLD.toString()
        GREEN = ChatColor.GREEN.toString()
        WHITE = ChatColor.WHITE.toString()
        BLACK = ChatColor.BLACK.toString()
        BOLD = ChatColor.BOLD.toString()
        ITALIC = ChatColor.ITALIC.toString()
        UNDER_LINE = ChatColor.UNDERLINE.toString()
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString()
        RESET = ChatColor.RESET.toString()
        MAGIC = ChatColor.MAGIC.toString()
        DARK_BLUE = ChatColor.DARK_BLUE.toString()
        DARK_AQUA = ChatColor.DARK_AQUA.toString()
        DARK_GRAY = ChatColor.DARK_GRAY.toString()
        DARK_GREEN = ChatColor.DARK_GREEN.toString()
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString()
        DARK_RED = ChatColor.DARK_RED.toString()
        PINK = ChatColor.LIGHT_PURPLE.toString()
        MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------"
        CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------"
        SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------"
    }

    fun getColorNames(): Set<String> {
        return MAP.keys
    }

    fun getColorFromName(name: String): ChatColor? {
        val trimmedName = name.trim().toLowerCase()
        if (MAP.containsKey(trimmedName)) {
            return MAP[trimmedName]
        }

        return try {
            ChatColor.valueOf(name.toUpperCase().replace(" ", "_"))
        } catch (e: Exception) {
            null
        }
    }

    fun translate(input: String): String {
        return ChatColor.translateAlternateColorCodes('&', input)
    }

    fun translate(lines: List<String>): List<String> {
        val toReturn: MutableList<String> = ArrayList()

        for (line in lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line))
        }

        return toReturn
    }

    fun translate(lines: Array<String>): List<String> {
        val toReturn: MutableList<String> = ArrayList()

        for (line in lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line))
        }

        return toReturn
    }
}

