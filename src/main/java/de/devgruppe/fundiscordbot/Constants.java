package de.devgruppe.fundiscordbot;

import com.google.gson.Gson;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by GalaxyHD on 22.09.2017.
 */
public class Constants {

  public static final Gson GSON = new Gson();
  public static final Random RANDOM = new Random();
  public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

}
