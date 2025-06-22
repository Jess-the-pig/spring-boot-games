package henrotaym.env.queues.listeners;

import henrotaym.env.annotations.KafkaRetryableListener;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.scheduler.GameCreation;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.QUEUE)
@RequiredArgsConstructor
public class GameCreatedListener implements Listener<GameCreatedEvent> {

  private final GameCreation gameCreation;
  private final List<String> receivedMessages = new CopyOnWriteArrayList<>();

  public List<String> getReceivedMessages() {
    return receivedMessages;
  }

  @Override
  @KafkaRetryableListener(GameCreatedEvent.EVENT_NAME)
  public void listen(GameCreatedEvent event) {
    log.info("Consumed event: {}", event);

    // TODO : convertir une partie vers GameRequest si besoin.
    gameCreation.addEvent(event);

    // Ajoute la représentation texte de l'event à la liste des messages reçus
    receivedMessages.add(event.toString());
  }
}
