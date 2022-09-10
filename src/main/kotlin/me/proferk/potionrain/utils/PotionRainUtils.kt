package me.proferk.potionrain.utils

import org.bukkit.DyeColor

object PotionRainUtils {
	val modifierCommands = mutableMapOf("frequency" to 20, "radius" to 15, "duration" to 25, "amplifier" to 0, "height" to 16, "speed" to 0)

	val COLORS = DyeColor.values().map { it.fireworkColor }
}