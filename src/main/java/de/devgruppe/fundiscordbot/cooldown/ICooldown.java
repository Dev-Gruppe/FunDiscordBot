package de.devgruppe.fundiscordbot.cooldown;

import net.dv8tion.jda.core.entities.Member;

/**
 * Created by GalaxyHD on 28.09.2017.
 * This class is used to configure a cooldown for a command
 */
public interface ICooldown {

  /**
   * This method is used to allow cooldown bypasses.
   *
   * @param member the member object from the JDA api.
   * @return if the member is allow to bypass the cooldown.
   */
  boolean bypassCooldown(Member member);

  /**
   * This method is used to set the cooldown duration.
   *
   * @return the duration from the cooldown in seconds.
   */
  int cooldownDuration();

}
