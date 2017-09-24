package de.devgruppe.fundiscordbot.command.commands.giphy;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GiphySchneidler {
  public static final String BASE_URL_TEMPLATE = "https://api.giphy.com/v1/gifs/%s?api_key=%s%s";
  public static final String[] REQUEST_HEADER = new String[]{"Accept", "application/json"};
  public static final String PRINT_GIF_FORMAT = "``#%s``\n%s";
  public static final int EXPECTED_RESPONSE_CODE = 200;

  public static MessageEmbed getEmbedGiphy(String title, String url) {
    final EmbedGifNames embedGifNames = FunDiscordBotStarter.getInstance().getConfig().getEmbedGifNames();
    new EmbedBuilder()
            					.setColor(Color.GREEN)
            					.setTitle(title)
            					.setImage(url)
            					.addField("ID","``#"+ id + "``",true)
            					.addField("Requested by",message.getAuthor().getName(),true)
            					.addField("URL", jsonObject.get("url").getAsString(),true)
            					.build()
  }

  @NoArgsConstructor
  @Getter
  public static class EmbedGifNames {
    private final String trendingGifTitle = "Trending GIF #%d";
    private final  randomGifTitle = "Zufälliges GIF";
    private final String idFieldKey = "ID";
    private final String authorKey = "Erstellt von";
    private final String urlKey = "URL";
  }
}
