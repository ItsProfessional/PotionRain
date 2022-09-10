package me.proferk.potionrain.commands

import org.bukkit.command.CommandExecutor

interface CommandExecutorWithName : CommandExecutor {
	val name: String
}