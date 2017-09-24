package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.util.HttpRequest;
import java.io.IOException;
import java.net.URLEncoder;
import net.dv8tion.jda.core.entities.Message;

public class GifCommand extends Command {

	public GifCommand() {
		super("gif", "<Tag>", "Sucht ein random Bild von giphy.com mit den entsprechendem Tag raus");
	}

	@Override
	public CommandResponse triggerCommand(Message message, String[] args) {
		try {
			if (args.length != 1) {
				return CommandResponse.SYNTAX_PRINTED;
			}
			if (FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey().equals("")) {
				message.getTextChannel().sendMessage("Der GIPHY-Api Token wurde noch nicht gesetzt!").queue();
				return CommandResponse.ACCEPTED;
			}
			String path = "https://api.giphy.com/v1/gifs/random";
			path += "?api_key=" + FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey();

			path += "&tag=" + URLEncoder.encode(args[0], "UTF-8");
			HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(path, HttpRequest.HttpRequestMethod.GET)
					.setReadTimeout(10000)
					.addHeader("Accept", "application/json"));
			JsonElement jsonElement = new JsonParser().parse(res.getResultMessage());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			jsonObject = jsonObject.getAsJsonObject("data");

			message.getTextChannel().sendMessage("``#" + jsonObject.get("id").getAsString() + "``\n" + jsonObject.get("image_url").getAsString()).queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResponse.ACCEPTED;
	}
}
