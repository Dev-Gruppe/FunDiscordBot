package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.utils.HttpRequest;
import java.awt.Color;
import java.io.IOException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class GifTrending extends Command {

	public GifTrending() {
		super("giftrending", "", "Zeigt dir random trending Gifs von Giphy.com an");
	}

	private int offset = 0;

	@Override
	public CommandResponse triggerCommand(Message message, String[] args) {
		try {
			String path = "https://api.giphy.com/v1/gifs/trending";
			path += "?api_key=" + FunDiscordBotStarter.getInstance().getConfig().getGiphyApiKey();
			path += "&limit=" + 1;
			path += "&offset=" + offset;

			HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(path, HttpRequest.HttpRequestMethod.GET)
					.setReadTimeout(10000)
					.addHeader("Accept", "application/json"));

			JsonElement jsonElement = new JsonParser().parse(res.getResultMessage());
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray jsonArray = jsonObject.getAsJsonArray("data");
			if (jsonArray.size() == 0) {
				message.getTextChannel().sendMessage("Trends konnten nicht geladen werden!").queue();
				return CommandResponse.ACCEPTED;
			}
			jsonObject = jsonArray.get(0).getAsJsonObject();
			String id = jsonObject.get("id").getAsString();
			jsonObject = jsonObject.getAsJsonObject("images");
			jsonObject = jsonObject.getAsJsonObject("original");

			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setColor(Color.GREEN)
					.setTitle("Trending gif #" + offset)
					.setImage(jsonObject.get("url").getAsString())
					.addField("ID","``#"+ id + "``",true)
					.addField("Requested by",message.getAuthor().getName(),true)
					.addField("URL", jsonObject.get("url").getAsString(),true)
					.build()).queue();
			offset++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResponse.ACCEPTED;
	}
}
