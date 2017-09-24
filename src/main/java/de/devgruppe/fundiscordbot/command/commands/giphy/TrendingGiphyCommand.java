package de.devgruppe.fundiscordbot.command.commands.giphy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.utils.HttpRequest;

import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public class TrendingGiphyCommand extends Command {
  private static final String GIPHY_SUB_PATH = "trending";
  private static final String ADDITIONAL_GIPHY_HEADERS = "&limit=1&offset=%d";

  private static final String JSON_ID_KEY = "id";

  public TrendingGiphyCommand() {
    super("giftrending", "", "Zeigt dir ein zufälliges trending Gif von Giphy.com an.");

  }

  private int offset = 0;

  @Override
  public CommandResponse triggerCommand(final Message message, final String[] args) {
    if (FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey().isEmpty()) {
      message.getTextChannel().sendMessage("Der GIPHY-Api Token wurde noch nicht gesetzt!").queue();
      FunDiscordBotStarter.getLogger().warn("The GIPHY-Api Token has not been set yet!");
      return CommandResponse.ACCEPTED;
    } else {
      this.performCommandAction(message);
    }
    return CommandResponse.ACCEPTED;
  }

  private void performCommandAction(final Message message) {
    try {
      final String url = String.format(GiphyConstants.BASE_URL_TEMPLATE, GIPHY_SUB_PATH,
              FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey(), String.format(ADDITIONAL_GIPHY_HEADERS, offset));
      final HttpRequest.RequestResponse response = HttpRequest.performRequest(new HttpRequest.RequestBuilder(url, HttpRequest.HttpRequestMethod.GET)
              .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getGiphyTimeout())
              .addHeader(GiphyConstants.REQUEST_HEADER[0], GiphyConstants.REQUEST_HEADER[1]));
      if (response.getStatus() == GiphyConstants.EXPECTED_RESPONSE_CODE) {
        final JsonElement jsonElement = new JsonParser().parse(response.getResultMessage());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        if (jsonArray.size() == 0) {
          this.handleNotFound(message);
        } else {
          jsonObject = jsonArray.get(0).getAsJsonObject();
          final String id = jsonObject.get(JSON_ID_KEY).getAsString();
          jsonObject = jsonObject.getAsJsonObject("images");
          jsonObject = jsonObject.getAsJsonObject("original");

          message.getTextChannel().sendMessage("``#" + id + "``\n" + jsonObject.get("url").getAsString()).queue();
          offset++;
        }
      } else if (response.getStatus() == GiphyConstants.NOT_FOUND_RESPONSE_CODE) {
        this.handleNotFound(message);
      } else {
        FunDiscordBotStarter.getLogger().warn("The giphy.com server returned an unexpected HTTP Status code (" + response.getStatus() + "): " + response.getResultMessage());
        message.getTextChannel().sendMessage("Der Server von giphy.com gab einen unerwarteten HTTP Status code zurück!").queue();
      }
    } catch (final IOException e) {
      FunDiscordBotStarter.getLogger().warn("An error occurred while getting a trending GIF from giphy.com.", e);
      message.getTextChannel().sendMessage("Das GIF konnte nicht geladen werden, da ein Fehler beim Laden auftrat.").queue();
    }
  }

  private void handleNotFound(final Message message) {
    if (offset == 0) {
      FunDiscordBotStarter.getLogger().warn("Trending GIF request to giphy.com returned no GIF!");
      message.getTextChannel().sendMessage("Es konnte leider kein GIF gefunden werden.").queue();
    } else {
      offset = 0;
      this.performCommandAction(message);
    }
  }
}
