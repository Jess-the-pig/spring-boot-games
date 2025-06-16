package henrotaym.env.queues.listeners;

import henrotaym.env.annotations.KafkaRetryableListener;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.http.requests.GameRequest;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.scheduler.GameCreation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.QUEUE)
@RequiredArgsConstructor
public class GameCreatedListener implements Listener<GameCreatedEvent> {

  private final GameCreation gamecreation;

  @Override
  @KafkaRetryableListener(GameCreatedEvent.EVENT_NAME)
  public void listen(GameCreatedEvent event) {
    log.info("consumed " + event.toString());
    GameRequest game = event.gamerequest();

    // TODO : convertir une partie vers gamerequest.
    gamecreation.addEvent(game);
  }
}
