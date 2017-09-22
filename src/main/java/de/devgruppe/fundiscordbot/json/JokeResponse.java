package de.devgruppe.fundiscordbot.json;

public class JokeResponse {

  private String type;
  private Joke[] value;

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Joke[] getValue() {
    return this.value;
  }

  public void setValue(Joke[] value) {
    this.value = value;
  }
}
