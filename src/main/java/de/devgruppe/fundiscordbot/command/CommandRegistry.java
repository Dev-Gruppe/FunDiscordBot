package de.devgruppe.fundiscordbot.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * This class is used to register/manage and assign events to commands.
 */
public interface CommandRegistry {
  /**
   * This method is used to register commands which are triggered by chat events.
   *
   * @param command the command which should be registered.
   * @return whether the command was successfully registered.
   */
  boolean registerCommand(Command command);

  /**
   * This method can be used to unregister commands in runtime.
   *
   * @param command the command which should be unregistered/removed.
   * @return whether the command was already registered and therefore successfully unregistered.
   */
  boolean unregisterCommand(Command command);

  /**
   * @return all registered commands via {@link CommandRegistry#registerCommand(Command)}
   */
  List<Command> getRegisteredCommands();

  /**
   * This method is called with an event object in order to hand over as much information as
   * possible. It filters the commands and calls their trigger methods.
   *
   * @param event an event object with the information.
   * @return a CommandResponse in order to react detailed to the response of trigger method.
   */
  CommandResponse triggerCommandByEvent(MessageReceivedEvent event);

  /**
   * This method is used to get the assigned command object for the given class. It can be used for
   * dynamic command names.
   *
   * @param commandClass the class which is used to get the assigned command object.
   * @return the command object
   */
  Command getCommandObjectByClass(Class<? extends Command> commandClass);
}
