package de.devgruppe.fundiscordbot.utils;

import net.dv8tion.jda.core.entities.Message;

import java.util.List;

public interface MessageCollector {

  void onStart();

  void onEnd(final List<Message> messages);

  void onMessageCollect(final Message message, final int position);

  boolean checkStop(final Message message, final int position);

}
