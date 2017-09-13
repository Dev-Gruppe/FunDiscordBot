package de.devgruppe.fundiscordbot.command;

import lombok.Getter;

@Getter
public enum CommandResponse {
  ACCEPTED(true), SYNTAX_PRINTED(true);

  private final boolean success;

  /**
   * @param success determines whether a matching command class was found or not.
   */
  CommandResponse(boolean success) {
    this.success = success;
  }
}
