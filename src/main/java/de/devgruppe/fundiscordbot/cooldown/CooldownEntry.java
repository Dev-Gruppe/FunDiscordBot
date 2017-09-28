package de.devgruppe.fundiscordbot.cooldown;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.core.entities.Member;

import java.util.HashMap;

/**
 * Created by GalaxyHD on 28.09.2017.
 */
public class CooldownEntry {

  private HashMap<String, Long> cooldowns = new HashMap<>();
  @Getter
  private int length;

  public CooldownEntry(int length) {
    this.length = length;
  }

  public void addMember(Member member) {
    if (!containsMember(member))
      cooldowns.put(member.getUser().getId(), System.currentTimeMillis());
  }

  public void removeMember(Member member){
    if(containsMember(member))
      cooldowns.remove(member.getUser().getId());
  }

  public boolean containsMember(Member member){
    return cooldowns.containsKey(member.getUser().getId());
  }

  public long getCooldownStart(Member member){
    if(!containsMember(member)) return -1;
    return cooldowns.get(member.getUser().getId());
  }

}
