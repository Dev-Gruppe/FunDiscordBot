package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.util.HttpRequest;
import java.io.IOException;
import java.net.URLEncoder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class GifCommand extends Command {

	private static final String API_URL = "https://api.giphy.com/v1/gifs/random";

	public GifCommand() {
		super("gif", "<Tag>", "Sucht ein random Bild von giphy.com mit den entsprechendem Tag raus");
	}

	@Override
	public CommandResponse triggerCommand(Message message, String[] args) {
		try {
			if (args.length == 0) {
				return CommandResponse.SYNTAX_PRINTED;
			}
			if (FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey().equals("")) {
				message.getTextChannel().sendMessage("Der GIPHY-Api Token wurde noch nicht gesetzt!").queue();
				return CommandResponse.ACCEPTED;
			}
			String tag = String.join(" ",args);

			String path = API_URL;
			path += "?api_key=" + FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey();

			path += "&tag=" + URLEncoder.encode(tag, "UTF-8");
			HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(path, HttpRequest.HttpRequestMethod.GET)
					.setReadTimeout(10000)
					.addHeader("Accept", "application/json"));
			JsonElement jsonElement = new JsonParser().parse(res.getResultMessage());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Object jsonUnknownObj = jsonObject.get("data");
			if (jsonUnknownObj instanceof JsonArray) {
				message.getTextChannel().sendMessage(String.format("Gif-Tag(``%s``) konnte nicht gefunden werden!", tag)).queue();
				return CommandResponse.ACCEPTED;
			}
			jsonObject = jsonObject.getAsJsonObject("data");

			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Random gif")
					.setImage(jsonObject.get("image_url").getAsString())
					.addField("ID", "``#" + jsonObject.get("id").getAsString() + "``", true)
					.addField("Requested by", message.getAuthor().getName(), true)
					.addField("Tag", "``" + tag + "``", true)
					.addField("URL", jsonObject.get("image_url").getAsString(), true)
					.build()).queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResponse.ACCEPTED;
	}
}
