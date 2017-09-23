package de.devgruppe.fundiscordbot.command.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.util.HttpRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;

public class MemeNamesCommand extends Command {

	public static final String API_URL = "https://memegen.link/";
	public static final String TEMPLATE_URL = "api/templates/";

	@Getter
	private List<String> memes;

	public MemeNamesCommand() {
		super("memes", "", "Gebe dir alle Meme-Namen aus");
		this.memes = new ArrayList<>();

		try {
			HttpRequest.RequestResponse res = HttpRequest.performRequest(new HttpRequest.RequestBuilder(API_URL + TEMPLATE_URL, HttpRequest.HttpRequestMethod.GET)
			.addHeader("User-Agent","Mozilla/5.0")
			.addHeader("Accept","application/json")
			.setReadTimeout(15000));
			JsonParser parser = new JsonParser();
			JsonObject jobject = parser.parse(res.getResultMessage()).getAsJsonObject();
			jobject.entrySet().stream()
					.map(Map.Entry::getValue)
					.map(jsonElement -> jsonElement.getAsString().replace(API_URL + TEMPLATE_URL, ""))
					.forEach(memes::add);

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
		System.out.println(sb.length());
		message.getTextChannel().sendMessage(sb.toString()).queue();
		return CommandResponse.ACCEPTED;
	}
}
