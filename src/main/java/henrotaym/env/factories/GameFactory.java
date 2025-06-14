package henrotaym.env.factories;

import henrotaym.env.entities.Game;
import henrotaym.env.repositories.GameRepository;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameFactory {

  private Faker faker;
  private GameRepository gameRepository;

  public Game make(Consumer<Game> callback) {
    Game game = new Game();
    game.setName(this.faker.naruto().character());
    callback.accept(game);
    return game;
  }

  public Game make() {
    return this.make((_) -> {});
  }

  public Game create(Consumer<Game> callback) {
    return this.gameRepository.save(this.make(callback));
  }

  public Game create() {
    return this.create((_) -> {});
  }
}
