package henrotaym.env.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import henrotaym.env.entities.Game;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrettyPrinter {
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public static void log(Game game) {
    log.info("Object:\n{}", gson.toJson(game));
  }

  public static String toJson(Game game) {
    return gson.toJson(game);
  }
}
