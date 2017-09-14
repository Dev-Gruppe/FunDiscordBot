package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;

import net.dv8tion.jda.core.entities.Message;

import java.util.StringJoiner;

public class EchoCommand extends Command {
  public EchoCommand() {
    super("echo", "<Nachricht>", "Dieser Befehl gibt deine zuvor geschriebene Nachricht aus.");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length > 0) {
      final StringJoiner echoMessage = new StringJoiner(" ");
      for (String argument : args) {
        echoMessage.add(argument);
      }
      message.getTextChannel().sendMessage(echoMessage.toString()).complete();
      return CommandResponse.ACCEPTED;
    } else {
      return CommandResponse.SYNTAX_PRINTED;
    }
  }
}
