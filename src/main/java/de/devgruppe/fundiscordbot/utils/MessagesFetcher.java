package de.devgruppe.fundiscordbot.utils;

import de.devgruppe.fundiscordbot.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagesFetcher {

  public static void getMessages(final TextChannel textChannel, final MessageCollector collector) {
    Constants.EXECUTOR_SERVICE.execute(() -> {
      final AtomicInteger counter = new AtomicInteger(0);
      collector.onStart();
      CollectResult result = getAllMessages(textChannel, collector, counter);
      final List<Message> messages = result.getMessages();
      messages.sort(Comparator.comparing(Message::getCreationTime));
      collector.onEnd(messages);
    });
  }

  private static CollectResult getAllMessages(final TextChannel textChannel, final MessageCollector collector,
      final AtomicInteger counter) {
    final List<Message> messages = new ArrayList<>();
    boolean forceBreak = false;
    for (final Message message : textChannel.getIterableHistory().cache(false)) {
      messages.add(message);
      counter.addAndGet(1);
      collector.onMessageCollect(message, counter.get());
      if (collector.checkStop(message, counter.get())) {
        forceBreak = true;
        break;
      }
    }
    return new CollectResult(messages, forceBreak);
  }


  @AllArgsConstructor
  @Getter
  private static class CollectResult {
    private final List<Message> messages;
    private boolean forceBreak;
  }

}
