package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

/**
 * Created by GalaxyHD on 23.09.2017.
 */
public class CountCommand extends Command {

  public CountCommand() {
    super("count", "<message>", "Wertet deine Nachricht aus.");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length < 1) {
      return CommandResponse.SYNTAX_PRINTED;
    }
    String messageString = String.join(" ", args);
    if (messageString.isEmpty()) return CommandResponse.SYNTAX_PRINTED;
    int chars = messageString.length();
    int words = messageString.split(" ").length;
    int sentences = messageString.split("[.!?]").length;

    message.getTextChannel().sendMessage(new EmbedBuilder()
        .setColor(Color.GREEN)
        .setTitle("Nachricht", null)
        .setDescription("```" + messageString + "```")
        .addField("", "Zeichen:\nWörter:\nSätze:", true)
        .addField("", String.format("`%s`\n`%s`\n`%s`", chars, words, sentences), true)
        .build()
    ).queue();

    return null;
  }
}
