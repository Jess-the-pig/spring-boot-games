package henrotaym.env.queues.listeners;

import henrotaym.env.annotations.KafkaRetryableListener;
import henrotaym.env.entities.Game;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.factories.GameFactory;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.utils.PrettyPrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.QUEUE)
@RequiredArgsConstructor
public class GameCreatedListener implements Listener<GameCreatedEvent> {

  private GameFactory gamefactory;

  @Override
  @KafkaRetryableListener(GameCreatedEvent.EVENT_NAME)
  public void listen(GameCreatedEvent event) {
    log.info("consumed " + event.getMessage());

    Game fakeGame = gamefactory.create();
    PrettyPrinter.log(fakeGame);
    PrettyPrinter.toJson(fakeGame);
  }
}
