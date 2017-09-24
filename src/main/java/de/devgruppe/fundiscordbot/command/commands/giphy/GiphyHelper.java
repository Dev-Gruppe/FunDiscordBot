package de.devgruppe.fundiscordbot.command.commands.giphy;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class GiphyHelper {
  public static final String BASE_URL_TEMPLATE = "https://api.giphy.com/v1/gifs/%s?api_key=%s%s";
  public static final String[] REQUEST_HEADER = new String[]{"Accept", "application/json"};
  public static final int EXPECTED_RESPONSE_CODE = 200;

  public static MessageEmbed buildEmbedGiphy(String title, String url, String requestedBy, String id) {
    return new EmbedBuilder()
            .setColor(Color.GREEN).setTitle(title).setImage(url)
            .addField("ID", "``#" + id + "``", true)
            .addField("Angefragt von", requestedBy, true)
            .build();
  }
}
