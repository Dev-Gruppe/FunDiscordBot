package de.devgruppe.fundiscordbot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by GalaxyHD on 27.07.2017.
 */
public class Configuration {
  private File configFile;
  @Getter(AccessLevel.PUBLIC)
  private Config config;
  private Gson gson;

  public Configuration() {
    this.configFile = new File("fundiscordbot-config.json");
    this.gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
  }

  public boolean exists() {
    return configFile.exists();
  }

  public void writeConfiguration() {
    writeConfiguration(config);
  }

  public void writeConfiguration(Config config) {
    try (FileWriter fileWriter = new FileWriter(configFile)) {
      gson.toJson(config, fileWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readConfiguration() {
    try (FileReader fileReader = new FileReader(configFile)) {
      config = gson.fromJson(fileReader, Config.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
