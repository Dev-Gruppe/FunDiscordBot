package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.Constants;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

/**
 * Created by florian_7843 on 23.09.2017.
 */
public class RandomCommand extends Command {

  public RandomCommand() {
    super("random", "<Zahl min> <Zahl max>", "Gibt dir eine Random Nummer aus.");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {

    if (args.length == 2) {
      if (isInt(args[0]) && isInt(args[1])) {
        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        if (max > min) {
          String msg = "Die Zufallsnummer Zwischen **" + min + "** und **" + max + "** ist **" + randomInt(min,
              max) + "**";
          message.getTextChannel().sendMessage(msg).queue();
          return CommandResponse.ACCEPTED;
        } else {
          String msg = "Die Zufallsnummer Zwischen **" + max + "** und **" + min + "** ist **" + randomInt(max,
              min) + "**";
          message.getTextChannel().sendMessage(msg).queue();
          return CommandResponse.ACCEPTED;
        }

      } else {
        return CommandResponse.SYNTAX_PRINTED;
      }
    } else if (args.length == 0) {
      int min = 1;
      int max = 6;

      String msg = "Die Zufallsnummer Zwischen **" + min + "** und **" + max + "** ist **" + randomInt(min, max) + "**";
      message.getTextChannel().sendMessage(msg).queue();
      return CommandResponse.ACCEPTED;
    } else {
      return CommandResponse.SYNTAX_PRINTED;
    }
  }

  private int randomInt(int min, int max) {
    Random r = Constants.RANDOM;
    return r.nextInt((max - min) + 1) + min;
  }

  private boolean isInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

}
