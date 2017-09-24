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
import java.net.URLEncoder;

public class RandomGiphyCommand extends Command {
  private static final String GIPHY_SUB_PATH = "random";
  private static final String ADDITIONAL_GIPHY_HEADERS = "&tag=%s";
  private static final String TAG_ENCODING = "UTF-8";

  private static final String EMBED_TITLE = "Zufälliges GIF";

  private static final String JSON_DATA_KEY = "data";
  private static final String JSON_ID_KEY = "id";
  private static final String JSON_IMAGE_URL_KEY = "image_url";

  public RandomGiphyCommand() {
    super("gif", "<Tag>", "Findet ein zufälliges GIF von giphy.com mit dem entsprechenden Tag heraus.");
  }

  @Override
  public CommandResponse triggerCommand(final Message message, final String[] args) {
    try {
      if (args.length == 0) {
        return CommandResponse.SYNTAX_PRINTED;
      }
      if (FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey().isEmpty()) {
        message.getTextChannel().sendMessage("Der GIPHY-Api Token wurde noch nicht gesetzt!").queue();
        FunDiscordBotStarter.getLogger().warn("The GIPHY-Api Token has not been set yet!");
      } else {
        this.performAcceptedCommand(message, args);
      }
      return CommandResponse.ACCEPTED;
    } catch (IOException e) {
      FunDiscordBotStarter.getLogger().warn("An error occurred while getting a random GIF from giphy.com.", e);
      message.getTextChannel().sendMessage("Das GIF konnte nicht geladen werden, da ein Fehler beim Laden auftrat.").queue();
    }
    return CommandResponse.ACCEPTED;
  }

  private void performAcceptedCommand(final Message message, final String[] args) throws IOException {
    String tag = String.join(" ", args);
    final String requestUrl = String.format(GiphyHelper.BASE_URL_TEMPLATE, GIPHY_SUB_PATH, FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey(),
            String.format(ADDITIONAL_GIPHY_HEADERS, URLEncoder.encode(tag, TAG_ENCODING)));
    final HttpRequest.RequestResponse response = HttpRequest.performRequest(new HttpRequest.RequestBuilder(requestUrl, HttpRequest.HttpRequestMethod.GET)
            .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getMemeTimeout())
            .addHeader(GiphyHelper.REQUEST_HEADER[0], GiphyHelper.REQUEST_HEADER[1]));
    if (response.getStatus() == GiphyHelper.EXPECTED_RESPONSE_CODE) {
      JsonElement jsonElement = new JsonParser().parse(response.getResultMessage());
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      Object unknownJsonObject = jsonObject.get(JSON_DATA_KEY);
      if (unknownJsonObject.getClass() == JsonArray.class) {
        message.getTextChannel().sendMessage("Es konnte leider kein passendes GIF gefunden werden.").queue();
        return;
      }
      jsonObject = jsonObject.getAsJsonObject(JSON_DATA_KEY);
      message.getTextChannel().sendMessage(GiphyHelper.buildEmbedGiphy(
              EMBED_TITLE, jsonObject.get(JSON_IMAGE_URL_KEY).getAsString(), message.getAuthor().getName(), jsonObject.get(JSON_ID_KEY).getAsString())).queue();
    } else {
      FunDiscordBotStarter.getLogger().warn("The giphy.com server returned an unexpected HTTP Status code (" + response.getStatus() + "): " + response.getResultMessage());
      message.getTextChannel().sendMessage("Der Server von giphy.com gab einen unerwarteten HTTP Status code zurück!").queue();
    }
  }
}
