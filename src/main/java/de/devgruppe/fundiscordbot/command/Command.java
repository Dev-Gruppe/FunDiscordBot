package de.devgruppe.fundiscordbot.command;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;

@Getter
public abstract class Command {
  private final String commandName, syntax, description;

  /**
   * This class is used to register his own commands.
   *
   * @param commandName the base command name.
   * @param syntax      the syntax which is shown to the user if something went wrong.
   * @param description a short and informative description about this command.
   */
  public Command(String commandName, String syntax, String description) {
    this.commandName = commandName;
    this.syntax = syntax;
    this.description = description;
  }

  /**
   * This method is called if an incoming message matches the command name.
   *
   * @param message the message object from the JDA api.
   * @param args    can be of size 0 and used to hand over some extra information.
   * @return a CommandResponse in order to react detailed to the response of trigger method.
   */
  public abstract CommandResponse triggerCommand(Message message, String[] args);
}
