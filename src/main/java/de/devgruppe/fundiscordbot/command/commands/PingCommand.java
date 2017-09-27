package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.Constants;
import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.entities.Message;

/**
 * Created by GalaxyHD on 26.09.2017.
 */
public class PingCommand extends Command {

  private static final int COUNT = 8;

  private static final String[] pingMessages = new String[]{
      ":desktop::white_small_square::black_small_square::black_small_square::earth_africa:",
      ":desktop::black_small_square::white_small_square::black_small_square::earth_africa:",
      ":desktop::black_small_square::black_small_square::white_small_square::earth_africa:",
      ":desktop::black_small_square::white_small_square::black_small_square::earth_africa:",
  };

  public PingCommand() {
    super("ping", "", "Gibt dir den Ping der WebSocket-Verbindung aus.");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("nice")) {
        Constants.EXECUTOR_SERVICE.execute(() -> message.getTextChannel().sendMessage("Ping...").queue(pingMessage -> {
          long lastResult;
          long sum = 0, min = 999, max = 0;
          for (int i = 0; i < COUNT; i++) {
            pingMessage.editMessage(pingMessages[i % pingMessages.length]).complete();
            lastResult = FunDiscordBotStarter.getInstance().getJda().getPing();
            sum += lastResult;
            min = Math.min(min, lastResult);
            max = Math.max(max, lastResult);
            try {
              Thread.sleep(1_500L);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          pingMessage.editMessage(
              String.format("Durchschnittlicher Ping %dms (min: %d, max: %d)", (int) Math.ceil(sum / COUNT), min, max))
              .complete();
        }));
        return CommandResponse.ACCEPTED;
      }
    }
    message.getTextChannel()
        .sendMessage(String.format("Ping: `%dms`", FunDiscordBotStarter.getInstance().getJda().getPing()))
        .complete();
    return CommandResponse.ACCEPTED;
  }

}
