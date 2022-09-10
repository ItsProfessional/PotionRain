package me.proferk.potionrain

import me.proferk.potionrain.commands.PotionRainCommand
import me.proferk.potionrain.listeners.WorldLoadListener
import me.proferk.potionrain.utils.PotionRainUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Method
import kotlin.reflect.full.primaryConstructor


class Main : JavaPlugin() {
	override fun onEnable() {
		PotionRainUtils.modifierCommands.keys.forEach {
			val cmd: BukkitCommand = object : BukkitCommand(it,"Set or get the $it of the potion rain", "/<command>", listOf()) {
				override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
					if(name !in PotionRainUtils.modifierCommands.keys) return true
					val capitalizedCmd = StringUtils.capitalize(name)
					val conjunction: String
					if (args.isNotEmpty()) {
						try {
							val result = args[0].toInt()
							PotionRainUtils.modifierCommands[name] = result
							conjunction = "has been"
						} catch (e: NumberFormatException) {
							sender.sendMessage("That is not a number!")
							return true
						}
					} else conjunction = "is"
					sender.sendMessage("$capitalizedCmd $conjunction set to " + PotionRainUtils.modifierCommands[name])
					return true
				}
			}
			val commandMap: Method = server.javaClass.getMethod("getCommandMap")
			val cmdMap: Any = commandMap.invoke(server)
			val register: Method = cmdMap.javaClass.getMethod("register", String::class.java, Command::class.java)
			register.invoke(cmdMap, cmd.name, cmd)
		}

		listOf(PotionRainCommand::class)
			.forEach {
				val command = it.primaryConstructor!!.call(this)
				getCommand(command.name)!!.setExecutor(command)
			}

		listOf(WorldLoadListener::class)
			.forEach {
				val listener = it.primaryConstructor!!.call(this)
				server.pluginManager.registerEvents(listener, this)
			}

		logger.info("Plugin has been enabled")
	}

	override fun onDisable() {
		logger.info("PotionRain plugin has been disabled")
	}
}