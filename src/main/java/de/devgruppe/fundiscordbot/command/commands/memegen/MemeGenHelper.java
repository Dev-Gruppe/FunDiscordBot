package de.devgruppe.fundiscordbot.command.commands.memegen;

public class MemeGenHelper {
  public static final String REQUEST_URL = "https://memegen.link/";
  public static final String TEMPLATE_PATH = "api/templates/";
  public static final String[] REQUEST_HEADERS = new String[]{
          "Accept", "application/json",
          "User-Agent", "Mozilla/5.0"
  };
  public static final int EXPECTED_RESPONSE_CODE = 200;
}
