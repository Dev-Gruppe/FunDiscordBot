package de.devgruppe.fundiscordbot.cooldown;

import de.devgruppe.fundiscordbot.FunDiscordBotStarter;
import de.devgruppe.fundiscordbot.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.core.entities.Member;

import java.util.HashMap;

/**
 * Created by GalaxyHD on 28.09.2017.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CooldownManager {
  @Getter
  private static CooldownManager instance = new CooldownManager();

  private HashMap<Command, CooldownEntry> commandCooldowns = new HashMap<>();

  public boolean hasCooldown(Command command, Member member) {
    if (!(command instanceof ICooldown)) return false;
    if (!commandCooldowns.containsKey(command)) return false;
    ICooldown cooldown = (ICooldown) command;
    if (cooldown.bypassCooldown(member)) return false;
    CooldownEntry cooldownEntry = commandCooldowns.get(command);
    if (!cooldownEntry.containsMember(member)) return false;
    long end = cooldownEntry.getLength() * 1000 + cooldownEntry.getCooldownStart(member);
    boolean flag = System.currentTimeMillis() < end;
    if (!flag) removeCooldown(command, member);
    return flag;
  }

  public void addCooldown(Command command, Member member) {
    if (!(command instanceof ICooldown)) return;
    ICooldown cooldown = (ICooldown) command;
    CooldownEntry cooldownEntry = null;
    if (!commandCooldowns.containsKey(command)) {
      cooldownEntry = commandCooldowns.put(command, new CooldownEntry(cooldown.cooldownLength()));
    }
    if (cooldownEntry == null) cooldownEntry = commandCooldowns.get(command);
    cooldownEntry.addMember(member);
    FunDiscordBotStarter.getLogger().debug("Add Cooldown to " + member.getNickname());
  }

  public void removeCooldown(Command command, Member member) {
    if (!(command instanceof ICooldown)) return;
    if (!commandCooldowns.containsKey(command)) return;
    CooldownEntry cooldownEntry = commandCooldowns.get(command);
    cooldownEntry.removeMember(member);
    FunDiscordBotStarter.getLogger().debug("Remove Cooldown from " + member.getNickname());
  }

}
