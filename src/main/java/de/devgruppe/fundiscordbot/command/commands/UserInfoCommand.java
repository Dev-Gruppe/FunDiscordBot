package de.devgruppe.fundiscordbot.command.commands;

import de.devgruppe.fundiscordbot.command.Command;
import de.devgruppe.fundiscordbot.command.CommandResponse;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.MiscUtil;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserInfoCommand extends Command {

  public UserInfoCommand() {
    super("userinfo", "<user>", "User information");
  }

  @Override
  public CommandResponse triggerCommand(Message message, String[] args) {
    if (args.length < 1)
      return CommandResponse.SYNTAX_PRINTED;
    Member member = getMember(message.getGuild(), args[0]);
    if (member == null) {
      message.getChannel().sendMessage("Member not found.").complete();
      return CommandResponse.ACCEPTED;
    }
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(member.getColor());
    User user = member.getUser();
    String onlineStatus = member.getOnlineStatus().name();
    String game = (member.getGame() != null ? member.getGame().getName() : "No game");
    String accountCreation = MiscUtil.getDateTimeString(MiscUtil.getCreationTime(user));

    List<Member> joins = new ArrayList<>(message.getGuild().getMembers());
    joins.sort(Comparator.comparing(Member:: getJoinDate));
    String joinPosition = String.valueOf(joins.indexOf(member) + 1);

    String joinDate = member.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    String joinOrder = getJoinOrder(member, joins);

    embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());

    embedBuilder.setAuthor(MessageFormat.format("Userinfo for {0} ({1})", user.getName(), user.getId()),
        user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
    embedBuilder.addField("Status", onlineStatus, true);
    embedBuilder.addField("Game", game, true);
    embedBuilder.addField("IsBot", String.valueOf(user.isBot()), true);
    embedBuilder.addField("Account Creation", accountCreation, true);
    embedBuilder.addField("Join Position", joinPosition, true);
    embedBuilder.addField("Join Date", joinDate, true);
    embedBuilder.addField("Nickname", member.getEffectiveName(), true);
    embedBuilder.addField("Join Order", joinOrder, true);
    message.getChannel().sendMessage(embedBuilder.build()).complete();
    return CommandResponse.ACCEPTED;
  }

  private String getJoinOrder(Member member, List<Member> joins) {
    StringBuilder joinOrder = new StringBuilder();
    int index = joins.indexOf(member);
    index -= 3;
    if (index < 0)
      index = 0;
    if (joins.get(index).equals(member))
      joinOrder.append("**").append(joins.get(index).getUser().getName()).append("**");
    else
      joinOrder.append(joins.get(index).getUser().getName());
    for (int i = index + 1; i < index + 7; i++) {
      if (i >= joins.size())
        break;
      Member joinMember = joins.get(i);
      String name = joinMember.getUser().getName();
      if (joinMember.equals(member))
        name = "**" + name + "**";
      joinOrder.append(" > ").append(name);
    }
    return joinOrder.toString();
  }

  private Member getMember(Guild guild, String name) {
    Optional<Member> optional = guild.
        getMembers()
        .stream()
        .filter(member -> member.getUser().getName().toLowerCase().startsWith(name.toLowerCase()) ||
            (member.getNickname() != null && member.getNickname().toLowerCase().startsWith(name.toLowerCase())))
        .findFirst();
    return optional.orElse(null);
  }

}
