package henrotaym.env.queues.listeners;

import henrotaym.env.annotations.KafkaRetryableListener;
import henrotaym.env.entities.Game;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.QUEUE)
public class GameCreatedListener implements Listener<GameCreatedEvent> {

  private final GameService gameService;

  public GameCreatedListener(GameService gameService) {
    this.gameService = gameService;
  }

  @Override
  @KafkaRetryableListener(GameCreatedEvent.EVENT_NAME)
  public void listen(GameCreatedEvent event) {
    log.info("consumed " + event.getMessage());

    // TODO: Mettre en place le request
    Game createdGame = gameService.store();
  }
}
