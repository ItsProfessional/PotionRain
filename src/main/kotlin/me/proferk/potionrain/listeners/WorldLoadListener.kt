package me.proferk.potionrain.listeners

import me.proferk.potionrain.tasks.RainTask
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.plugin.java.JavaPlugin

class WorldLoadListener(private val plugin: JavaPlugin) : Listener {
	@EventHandler
	fun onWorldLoad(e: WorldLoadEvent){
		RainTask().runTaskTimer(plugin, 0L, 4L)
	}
}