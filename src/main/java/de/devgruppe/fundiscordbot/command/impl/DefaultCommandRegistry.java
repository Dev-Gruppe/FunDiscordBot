package de.devgruppe.fundiscordbot.command.impl;

import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandRegistry;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GalaxyHD on 11.09.2017.
 */
public class DefaultCommandRegistry extends ListenerAdapter implements CommandRegistry {

  private static final String PREFIX = "!";
  private List<Command> commands = new ArrayList<>();

  @Override
  public boolean registerCommand(Command command) {
    return this.commands.add(command);
  }

  @Override
  public boolean unregisterCommand(Command command) {
    return this.commands.remove(command);
  }

  @Override
  public List<Command> getRegisteredCommands() {
    return this.commands;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    if (!event.isFromType(ChannelType.TEXT)) return;
    Message message = event.getMessage();
    String content = message.getContent().trim();
    String[] splitted = content.contains(" ") ? content.split(" ") : new String[]{content};
    if (!content.startsWith(PREFIX)) return;
    String commandName = splitted[0].substring(PREFIX.length(), splitted[0].length());
    String[] args = Arrays.copyOfRange(splitted, 1, splitted.length);
    Command command = getCommandObjectByName(commandName);
    if (command == null) {
      message.getTextChannel().sendMessage("Der Command wurde nicht gefunden.").complete();
      return;
    }
    CommandResponse commandResponse = command.triggerCommand(message, args);
    if (commandResponse == CommandResponse.SYNTAX_PRINTED)
      message.getTextChannel()
          .sendMessage(
              MessageFormat.format("Syntax: `{0}{1} {2}`", PREFIX, command.getCommandName(), command.getSyntax()))
          .complete();
  }

  @Override
  public Command getCommandObjectByClass(Class<? extends Command> commandClass) {
    return this.commands.stream().filter(command -> command.getClass().equals(commandClass)).findFirst().orElse(null);
  }

  @Override
  public Command getCommandObjectByName(String commandName) {
    return this.commands.stream()
        .filter(command -> command.getCommandName().equalsIgnoreCase(commandName))
        .findFirst()
        .orElse(null);
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }

}
