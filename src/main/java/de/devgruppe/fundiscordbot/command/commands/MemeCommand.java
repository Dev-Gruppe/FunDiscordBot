package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.util.HttpRequest;
import java.io.IOException;
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
			JsonParser parser = new JsonParser();
			StringBuilder urlRequest = new StringBuilder();
			urlRequest.append(args[0]).append("/");
			if (args.length >= 2) {
				StringBuilder texts = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					texts.append(args[i]).append(" ");
				}
				String textString = texts.toString();
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
					urlRequest.append(texts.toString());
				}
				HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(MemeNamesCommand.API_URL + MemeNamesCommand.TEMPLATE_URL + urlRequest.toString(), HttpRequest.HttpRequestMethod.GET)
						.addHeader("User-Agent", "Mozilla/5.0")
						.addHeader("Accept", "application/json")
						.setReadTimeout(15000));

				JsonElement jsonElement = new JsonParser().parse(res.getResultMessage());
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				jsonObject = jsonObject.getAsJsonObject("direct");

				message.getTextChannel().sendMessage(jsonObject.get("masked").getAsString()).queue();
				return CommandResponse.ACCEPTED;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResponse.SYNTAX_PRINTED;
	}
}
