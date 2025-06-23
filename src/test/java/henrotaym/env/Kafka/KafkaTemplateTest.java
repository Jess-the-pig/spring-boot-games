package henrotaym.env.Kafka;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import henrotaym.env.ApplicationTest;
import henrotaym.env.database.factories.GameFactory;
import henrotaym.env.entities.Game;
import henrotaym.env.enums.EventName;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.queues.listeners.GameCreatedListener;
import henrotaym.env.services.GameCreatedService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@ExtendWith(MockitoExtension.class)
class KafkaTemplateTest extends ApplicationTest {
  @Autowired KafkaTemplate<String, String> kafkaTemplate;
  @Autowired GameFactory gameFactory;
  String eventName = EventName.GAME_CREATED;

  @Mock private GameCreatedService gameCreatedService;

  @InjectMocks private GameCreatedListener gameCreatedListener;

  @Test
  void it_should_send_a_gamerequest_to_listener() {

    Game game = gameFactory.create();
    String serializedGame = null;

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      serializedGame = objectMapper.writeValueAsString(game);
    } catch (JsonProcessingException e) {
      fail("Erreur lors de la sérialisation du game : " + e.getMessage());
    }

    GameCreatedEvent event = new GameCreatedEvent(serializedGame.getBytes());

    // WHEN
    gameCreatedListener.listen(event);

    // THEN
    verify(gameCreatedService, times(1)).enqueue(any(GameCreatedEvent.class));
  }
}
