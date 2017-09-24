package de.devgruppe.fundiscordbot.command.commands.memegen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.utils.HttpRequest;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

public class MemeGenListCommand extends Command {
  @Getter
  private final List<String> memes = new ArrayList<>();

  public MemeGenListCommand() {
    super("memes", "", "Gebe dir alle Meme-Namen aus.");
    try {
      HttpRequest.RequestResponse response = HttpRequest.performRequest(new HttpRequest.RequestBuilder(
              MemeGenHelper.REQUEST_URL + MemeGenHelper.TEMPLATE_PATH, HttpRequest.HttpRequestMethod.GET)
              .addHeader(MemeGenHelper.REQUEST_HEADERS[0], MemeGenHelper.REQUEST_HEADERS[1])
              .addHeader(MemeGenHelper.REQUEST_HEADERS[2], MemeGenHelper.REQUEST_HEADERS[3])
              .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getMemeTimeout()));
      if (response.getStatus() == MemeGenHelper.EXPECTED_RESPONSE_CODE) {
        final JsonParser parser = new JsonParser();
        final JsonObject jsonObject = parser.parse(response.getResultMessage()).getAsJsonObject();
        jsonObject.entrySet().stream().map(Map.Entry::getValue).map(jsonElement -> jsonElement.getAsString()
                .replace(MemeGenHelper.REQUEST_URL + MemeGenHelper.TEMPLATE_PATH, "")).forEach(memes::add);
      } else {
        FunDiscordBotStarter.getLogger().warn("The memegen.link server returned an unexpected HTTP Status code (" + response.getStatus() + "): " + response.getResultMessage());
      }
    } catch (IOException e) {
      FunDiscordBotStarter.getLogger().error("An error occurred while fetching the meme names.", e);
    }
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    StringJoiner listJoiner = new StringJoiner(", ");
    memes.forEach(s -> listJoiner.add("``" + s + "``"));
    message.getTextChannel().sendMessage("Folgende Memes können generiert werden: \n" + listJoiner.toString())
            .queue(msg -> msg.delete().queueAfter(20, TimeUnit.SECONDS));
    return CommandResponse.ACCEPTED;
  }
}
