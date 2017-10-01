package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandRegistry;
import de.devgruppe.fundiscordbot.command.CommandResponse;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

/**
 * Created by GalaxyHD on 12.09.2017.
 */
public class CommandListCommand extends Command {
  public CommandListCommand() {
    super("commandlist", "", "Gibt dir eine Übersicht von allen Befehlen aus.");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    final CommandRegistry commandRegistry = FunDiscordBotStarter.getInstance().getCommandRegistry();
    final EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.GRAY).setTitle("Befehlsübersicht");
    commandRegistry.getRegisteredCommands().forEach(command ->
            embedBuilder.addField(commandRegistry.getPrefix() + command.getCommandName(), command.getDescription(), false));
    //embedBuilder.setFooter("Seite 1", null);
    message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    return CommandResponse.ACCEPTED;
  }
}
