package de.devgruppe.fundiscordbot.json;

public class Joke {
  private int id;
  private String[] categories;
  private String joke;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String[] getCategories() {
    return this.categories;
  }

  public void setCategories(String[] categories) {
    this.categories = categories;
  }

  public String getJoke() {
    return this.joke;
  }

  public void setJoke(String joke) {
    this.joke = joke;
  }
}
