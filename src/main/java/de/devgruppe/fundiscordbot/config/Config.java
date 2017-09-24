package de.devgruppe.fundiscordbot.config;

import lombok.Data;

/**
 * Created by GalaxyHD on 27.07.2017.
 */
@Data
public class Config {
  private String botToken = "";
  private String giphyApiKey = "";
  private int giphyTimeout = 3000;
  private int memeTimeout = 8000;
}
