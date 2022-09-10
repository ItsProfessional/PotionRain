package me.proferk.potionrain.tasks

import me.proferk.potionrain.utils.PotionRainUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.ThrownPotion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class RainTask : BukkitRunnable() {
	override fun run() {
		for (player in Bukkit.getOnlinePlayers()) {
			val rnd = Random()

			for (i in 0 until PotionRainUtils.modifierCommands["frequency"]!!) {
				val rndPot = rnd.nextInt(PotionEffectType.values().size - 1)
				val rndColor = rnd.nextInt(PotionRainUtils.COLORS.size - 1)
				val rndAmplifier: Int =
					if (PotionRainUtils.modifierCommands["amplifier"] == 0) 0 else rnd.nextInt(PotionRainUtils.modifierCommands["amplifier"]!!)
				val loc = player.location
				val world = player.world
				val potion = ItemStack(Material.SPLASH_POTION)
				val potionMeta = potion.itemMeta as PotionMeta
				val potionEffect = PotionEffect(
					PotionEffectType.values()[rndPot],
					PotionRainUtils.modifierCommands["duration"]!!,
					rndAmplifier,
					false,
					true,
					true
				)
				potionMeta.addCustomEffect(potionEffect, true)
				potionMeta.color = PotionRainUtils.COLORS[rndColor]
				potion.itemMeta = potionMeta
				if (PotionRainUtils.modifierCommands["radius"] == 0) PotionRainUtils.modifierCommands["radius"] = 1
				var rndX = rnd.nextInt(PotionRainUtils.modifierCommands["radius"]!!)
				var rndZ = rnd.nextInt(PotionRainUtils.modifierCommands["radius"]!!)
				val rndXNeg = rnd.nextBoolean()
				val rndZNeg = rnd.nextBoolean()
				if (rndXNeg) rndX *= -1
				if (rndZNeg) rndZ *= -1
				val newLoc = loc.clone()
				newLoc.x = loc.x + rndX
				newLoc.y = loc.y + PotionRainUtils.modifierCommands["height"]!!
				newLoc.z = loc.z + rndZ
				val thrownPotion = world.spawnEntity(newLoc, EntityType.SPLASH_POTION) as ThrownPotion
				thrownPotion.item = potion
				thrownPotion.velocity =
					Vector(0, PotionRainUtils.modifierCommands["speed"]!! * -1, 0)
			}
		}
	}
}