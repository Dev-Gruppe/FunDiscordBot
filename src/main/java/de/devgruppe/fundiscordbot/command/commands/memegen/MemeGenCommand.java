package de.devgruppe.fundiscordbot.command.commands.meme;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.utils.HttpRequest;

import net.dv8tion.jda.core.entities.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class MemeCommand extends Command {
  private static final String TEMPLATE_PATH =""

  public MemeCommand() {
    super("meme", "<Name> [<Text oben>[;<Text unten>]]", "Erstellt einen Meme");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length == 0) {
      return CommandResponse.SYNTAX_PRINTED;
    }
    return this.performMemeRequest(message, args);
  }

  private CommandResponse performMemeRequest(Message message, String[] args) {
    try {
      if (args.length < 1) {
        return CommandResponse.SYNTAX_PRINTED;
      }
      StringBuilder urlRequest = new StringBuilder();
      urlRequest.append(args[0]).append("/");
      if (args.length >= 2) {
        String text = this.getEscapedText(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        String[] splitText = text.split(";");
        if (splitText.length == 2) {
          urlRequest.append(splitText[0]).append("/").append(splitText[1]);
        } else if (splitText.length == 1) {
          urlRequest.append(text);
        } else {
          message.getTextChannel().sendMessage("Es darf maximal ein Semikolon(``;``) in deine Nachricht enthalten sein.").queue();
          return CommandResponse.ACCEPTED;
        }
        HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(
                MemeNamesCommand.API_URL + MemeNamesCommand.TEMPLATE_URL + urlRequest.toString().toLowerCase(), HttpRequest.HttpRequestMethod.GET)
                .addHeader(MemeConstants.REQUEST_HEADERS[0], MemeConstants.REQUEST_HEADERS[1])
                .addHeader(MemeConstants.REQUEST_HEADERS[2], MemeConstants.REQUEST_HEADERS[3])
                .setReadTimeout(FunDiscordBotStarter.getInstance().getConfig().getMemeTimeout()));

        JsonElement jsonElement = new JsonParser().parse(res.getResultMessage());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("direct");

        String[] splitUrlReq = urlRequest.toString().split("/");
        String topText = splitUrlReq[1];
        String bottomText = "-";
        if (splitUrlReq.length > 2)
          bottomText = splitUrlReq[2];
        message.getTextChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(" ")
                .addField("Name", args[0], true)
                .addField("Top text", topText, true)
                .addField("Bottom text", bottomText, true)
                .addField("Created by", message.getAuthor().getName(), true)
                .addField("URL", jsonObject.get("masked").getAsString(), true)
                .setImage(jsonObject.get("masked").getAsString())
                .build()).queue();

        return CommandResponse.ACCEPTED;
      } else {
        return CommandResponse.SYNTAX_PRINTED;
      }
    } catch (FileNotFoundException exe) {
      message.getTextChannel().sendMessage("Meme konnte nicht gefunden werden! Versuche ``!memes``").queue();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
