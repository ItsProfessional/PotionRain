package me.proferk.potionrain.commands

import me.proferk.potionrain.tasks.RainTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class PotionRainCommand(private val plugin: JavaPlugin) : CommandExecutorWithName {
	override val name: String = "potionRain"

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
		if(args.isEmpty()){
			sender.sendMessage(Component.text("Please provide an argument, i.e. Start(1) OR Stop(0)").color(NamedTextColor.RED))
			return true
		}

		when(args[0].lowercase()){
			"start", "1" -> {
				RainTask().runTaskTimer(plugin, 0L, 4L)
				sender.sendMessage(Component.text("PotionRain has been started!").color(NamedTextColor.BLUE))
			}
			"stop", "0" -> {
				plugin.server.scheduler.cancelTasks(plugin)
				sender.sendMessage(Component.text("PotionRain has been stopped!").color(NamedTextColor.GOLD))
			}
			else -> sender.sendMessage(Component.text("Invalid argument. Please provide a valid argument, i.e. Start(1) OR Stop(0)").color(NamedTextColor.RED))
		}

		return true
	}
}