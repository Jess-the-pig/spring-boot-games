package henrotaym.env.queues.listeners;

import henrotaym.env.annotations.KafkaRetryableListener;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.services.GameCreatedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.QUEUE)
@RequiredArgsConstructor
public class GameCreatedListener implements Listener<GameCreatedEvent> {

  private GameCreatedService gamecreatedservice;

  // Le listener Kafka
  @KafkaRetryableListener(GameCreatedEvent.EVENT_NAME)
  public void listen(GameCreatedEvent gameCreatedEvent) {
    // Affiche ou loggue les informations
    log.info("Game reçu du topic {}: {}", gameCreatedEvent.eventName(), gameCreatedEvent.getData());

    gamecreatedservice.enqueue(gameCreatedEvent);
  }
}
