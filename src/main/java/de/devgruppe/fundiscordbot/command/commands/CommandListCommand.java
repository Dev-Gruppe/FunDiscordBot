package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandRegistry;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.entities.Message;

/**
 * Created by GalaxyHD on 12.09.2017.
 */
public class CommandListCommand extends Command {

  public CommandListCommand() {
    super("commandlist", "", "Gibt dir eine Übersicht von allen Befehlen");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    StringBuilder stringBuilder = new StringBuilder();
    CommandRegistry commandRegistry = FunDiscordBotStarter.getInstance().getCommandRegistry();
    stringBuilder.append("Befehle: \n");
    stringBuilder.append("```");
    commandRegistry.getRegisteredCommands().forEach(command -> stringBuilder.append(commandRegistry.getPrefix())
        .append(command.getCommandName())
        .append(" - ")
        .append(command.getDescription())
        .append("\n"));
    stringBuilder.append("```");
    message.getTextChannel().sendMessage(stringBuilder.toString()).complete();
    return CommandResponse.ACCEPTED;
  }
}
