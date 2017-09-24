package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.util.HttpRequest;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class MemeCommand extends Command {

	public MemeCommand() {
		super("meme", "<Name> [<Text oben>[;<Text unten>]]", "Erstellt einen Meme");
	}

	@Override
	public CommandResponse triggerCommand(Message message, String[] args) {
		try {
			if (args.length < 1) {
				return CommandResponse.SYNTAX_PRINTED;
			}
			StringBuilder urlRequest = new StringBuilder();
			urlRequest.append(args[0]).append("/");
			if (args.length >= 2) {
				String textString = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

				textString = textString.replace("/", "~s");
				textString = textString.replace("?", "~q");
				textString = textString.replace("%", "~p");
				textString = textString.replace("#", "~h");
				textString = textString.replace("\"", "''");

				if (textString.contains(";")) {
					int count = textString.length() - textString.replace(";", "").length();
					if (count > 1) {
						message.getTextChannel().sendMessage("Du darfst maximal ein Semikolon(``;``) in deine Nachricht schreiben!").complete();
						return CommandResponse.ACCEPTED;
					}
					String[] splitText = textString.split(";");
					urlRequest.append(splitText[0]).append("/").append(splitText[1]);
				} else {
					urlRequest.append(textString);
				}
				HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(MemeNamesCommand.API_URL + MemeNamesCommand.TEMPLATE_URL + urlRequest.toString(), HttpRequest.HttpRequestMethod.GET)
						.addHeader("User-Agent", "Mozilla/5.0")
						.addHeader("Accept", "application/json")
						.setReadTimeout(15000));

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
		return CommandResponse.ACCEPTED;
	}
}
