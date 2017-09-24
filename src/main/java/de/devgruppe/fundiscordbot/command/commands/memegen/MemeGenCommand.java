package de.devgruppe.fundiscordbot.command.commands.memegen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.command.commands.giphy.GiphyHelper;
import de.devgruppe.fundiscordbot.utils.HttpRequest;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class MemeGenCommand extends Command {
  private static final String JSON_DIRECT_KEY = "direct";
  private static final String JSON_MASKED_KEY = "masked";

  public MemeGenCommand() {
    super("meme", "<Name> <Text oben>[;<Text unten>]", "Erstellt einen Meme");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length == 0) {
      return CommandResponse.SYNTAX_PRINTED;
    }
    return this.performMemeRequest(message, args);
  }

  private CommandResponse performMemeRequest(final Message message, final String[] args) {
    try {
      if (args.length < 1) {
        return CommandResponse.SYNTAX_PRINTED;
      }
      final StringBuilder urlRequest = new StringBuilder();
      urlRequest.append(args[0]).append("/");
      if (args.length >= 2) {
        final String text = this.getEscapedText(String.join(" ", Arrays.copyOfRange(args, 1, args.length)))
                .replaceAll(" +", " ");
        final String[] splitText = text.split(";");
        if (splitText.length == 2) {
          urlRequest.append(splitText[0]).append("/").append(splitText[1]);
        } else if (splitText.length == 1) {
          urlRequest.append(text);
        } else {
          message.getTextChannel().sendMessage("Es darf maximal ein Semikolon(``;``) in deine Nachricht enthalten sein.").queue();
          return CommandResponse.ACCEPTED;
        }
        final HttpRequest.RequestResponse response = HttpRequest.performRequest(new HttpRequest.RequestBuilder(
                MemeGenHelper.REQUEST_URL + MemeGenHelper.TEMPLATE_PATH + urlRequest.toString().toLowerCase(), HttpRequest.HttpRequestMethod.GET)
                .addHeader(MemeGenHelper.REQUEST_HEADERS[0], MemeGenHelper.REQUEST_HEADERS[1])
                .addHeader(MemeGenHelper.REQUEST_HEADERS[2], MemeGenHelper.REQUEST_HEADERS[3])
                .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getMemeTimeout()));
        if (response.getStatus() == GiphyHelper.EXPECTED_RESPONSE_CODE) {
          final JsonElement jsonElement = new JsonParser().parse(response.getResultMessage());
          final JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject(JSON_DIRECT_KEY);
          final String url = jsonObject.get(JSON_MASKED_KEY).getAsString();
          message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle(" ").setImage(url)
                  .addField("Meme-Name", args[0], true)
                  .addField("Erstellt von", message.getAuthor().getName(), true)
                  .addField("URL", url, true).build()).queue();
          return CommandResponse.ACCEPTED;
        } else {
          FunDiscordBotStarter.getLogger().warn("The memegen.link server returned an unexpected HTTP Status code (" + response.getStatus() + "): " + response.getResultMessage());
          message.getTextChannel().sendMessage("Der Server von memegen.link gab einen unerwarteten HTTP Status code zurück!").queue();
        }
      } else {
        return CommandResponse.SYNTAX_PRINTED;
      }
    } catch (final FileNotFoundException exe) {
      message.getTextChannel().sendMessage("Meme konnte nicht gefunden werden! Versuche ``!memes``").queue();
    } catch (final IOException e) {
      FunDiscordBotStarter.getLogger().warn("Unexpected error on Meme request.", e);
      message.getTextChannel().sendMessage("Das Meme konnte auf Grund eines Fehlers nicht erstellt werden.").queue();
    }
    return CommandResponse.ACCEPTED;
  }

  private String getEscapedText(String textString) {
    textString = textString.replace("/", "~s");
    textString = textString.replace("?", "~q");
    textString = textString.replace("%", "~p");
    textString = textString.replace("#", "~h");
    textString = textString.replace("\"", "''");
    return textString;
  }
}
