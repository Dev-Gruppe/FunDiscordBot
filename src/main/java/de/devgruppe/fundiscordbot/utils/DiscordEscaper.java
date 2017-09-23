package de.devgruppe.fundiscordbot.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GalaxyHD on 06.07.2017.
 */
public class DiscordEscaper {

  private static final List<Character> ESCAPE_CHARACTERS = new ArrayList<Character>() {{
    this.add('*');
    this.add('_');
    this.add('`');
    this.add('~');
  }};

  public static String escape(final String input) {
    final StringBuilder stringBuilder = new StringBuilder(input);
    for (int i = 0; i < stringBuilder.length(); i++) {
      final char c = stringBuilder.charAt(i);
      if (ESCAPE_CHARACTERS.contains(c)) {
        stringBuilder.replace(i, i + 1, "\\" + c);
        i += 1;
      }
    }
    return stringBuilder.toString();
  }
}
