package de.devgruppe.fundiscordbot.command.commands.meme;

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

import lombok.Getter;

public class MemeListCommand extends Command {
  public static final String API_URL = "https://memegen.link/";
  public static final String TEMPLATE_URL = "api/templates/";

  @Getter
  private final List<String> memes = new ArrayList<>();

  public MemeListCommand() {
    super("memes", "", "Gebe dir alle Meme-Namen aus.");
    try {
      HttpRequest.RequestResponse response = HttpRequest.performRequest(new HttpRequest.RequestBuilder(API_URL + TEMPLATE_URL, HttpRequest.HttpRequestMethod.GET)
              .addHeader(MemeConstants.REQUEST_HEADERS[0], MemeConstants.REQUEST_HEADERS[1])
              .addHeader(MemeConstants.REQUEST_HEADERS[2], MemeConstants.REQUEST_HEADERS[3])
              .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getMemeTimeout()));
      if (response.getStatus() == MemeConstants.EXPECTED_RESPONSE_CODE) {
        final JsonParser parser = new JsonParser();
        final JsonObject jsonObject = parser.parse(response.getResultMessage()).getAsJsonObject();
        jsonObject.entrySet().stream().map(Map.Entry::getValue).map(jsonElement -> jsonElement.getAsString()
                .replace(API_URL + TEMPLATE_URL, "")).forEach(memes::add);
      } else {

      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    StringBuilder sb = new StringBuilder();
    sb.append("Folgende Memes können generiert werden");
    sb.append("```");
    memes.forEach(s -> sb.append(s).append("\n"));
    sb.append("```");
    message.getTextChannel().sendMessage(sb.toString()).queue();
    return CommandResponse.ACCEPTED;
  }
}
