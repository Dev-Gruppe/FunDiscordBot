package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.Constants;
import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import de.devgruppe.fundiscordbot.json.Joke;
import de.devgruppe.fundiscordbot.json.JokeResponse;
import de.devgruppe.fundiscordbot.utils.DiscordEscaper;
import net.dv8tion.jda.core.entities.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by GalaxyHD on 22.09.2017.
 */
public class JokeCommand extends Command {

  private ArrayList<Joke> jokes = new ArrayList<>();

  //"http://api.icndb.com/jokes/

  public JokeCommand() {
    super("joke", "", "Gibt dir einen Nerd Witz aus.");
    new Thread(() -> {
      try {
        URL url = new URL("http://api.icndb.com/jokes/");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(10 * 1000);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine = bufferedReader.readLine();
        JokeResponse jokeResponse = Constants.GSON.fromJson(inputLine, JokeResponse.class);
        for (Joke joke : jokeResponse.getValue()) {
          for (String s : joke.getCategories()) {
            if(s.equalsIgnoreCase("nerdy")) {
              this.jokes.add(joke);
              break;
            }
          }
        }
        System.out.println("Jokes: " + this.jokes.size() + "/" + jokeResponse.getValue().length);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    Joke joke = this.jokes.get(Constants.RANDOM.nextInt(this.jokes.size()));
    String jokeString = DiscordEscaper.escape(joke.getJoke().contains("Chuck Norris") ? joke.getJoke().replace("Chuck Norris", message.getAuthor().getName()) : joke.getJoke());
    message.getTextChannel().sendMessage(jokeString).complete();
    return CommandResponse.ACCEPTED;
  }
}
