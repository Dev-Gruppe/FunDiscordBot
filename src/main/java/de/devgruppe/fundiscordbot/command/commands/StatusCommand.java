package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Message;

public class StatusCommand extends Command {

  public StatusCommand() {
    super("status", "", "Bot information");
  }

  @Override
  public CommandResponse triggerCommand(final Message message, final String[] args) {
    JDA jda = message.getJDA();
    final FunDiscordBotStarter instance = FunDiscordBotStarter.getInstance();
    EmbedBuilder embedBuilder = new EmbedBuilder()
        .setAuthor("Information", null, jda.getSelfUser().getEffectiveAvatarUrl())
        .setColor(message.getGuild().getSelfMember().getColor());
    embedBuilder.addField("JDA Version", JDAInfo.VERSION, true);
    embedBuilder.addField("Java Version", System.getProperty("java.runtime.version").replace("+", "_"), true);
    embedBuilder.addField("Guilds", String.valueOf(jda.getGuilds().size()), true);
    embedBuilder.addField("Ping", String.valueOf(jda.getPing()), true);
    embedBuilder.addField("Total Responses", String.valueOf(jda.getResponseTotal()), true);
    embedBuilder
        .addField("Commands", String.valueOf(instance.getCommandRegistry().getRegisteredCommands().size()), true);
    embedBuilder.addField("Total Users", String.valueOf(jda.getUserCache().size()), true);
    embedBuilder.addField("JDA Status", jda.getStatus().name(), true);
    embedBuilder.addField("WebSocketTrace", String.valueOf(jda.getWebSocketTrace().size()), true);
    embedBuilder.addField("CloudflareRays", String.valueOf(jda.getCloudflareRays().size()), true);
    message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    return CommandResponse.ACCEPTED;
  }
}
