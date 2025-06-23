package henrotaym.env.Kafka;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import henrotaym.env.ApplicationTest;
import henrotaym.env.database.factories.GameFactory;
import henrotaym.env.deserializer.GameDeserializer;
import henrotaym.env.entities.Game;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.repositories.GameRepository;
import henrotaym.env.scheduler.GameCreation;
import henrotaym.env.services.GameCreatedService;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerTest extends ApplicationTest {
  private ObjectMapper objectMapper;
  @Autowired private GameFactory gameFactory;

  @Mock private GameRepository gameRepository;
  @Mock private GameDeserializer gameDeserializer;

  @InjectMocks private GameCreatedService gameCreatedService;

  private GameCreation scheduler;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();

    scheduler = new GameCreation(gameCreatedService);
  }

  @Test
  void handle_shouldProcessThreeGames() throws Exception {
    for (int i = 0; i < 3; i++) {
      // Création d’un jeu avec factory
      Game originalGame = gameFactory.create();

      // Sérialisation
      String serializedGame = null;
      try {
        serializedGame = objectMapper.writeValueAsString(originalGame);
      } catch (Exception e) {
        fail("Erreur de sérialisation : " + e.getMessage());
      }

      byte[] serializedData = serializedGame.getBytes(StandardCharsets.UTF_8);

      // Mock de l'événement
      GameCreatedEvent event = mock(GameCreatedEvent.class);
      when(event.eventName()).thenReturn("GAME_CREATED");
      when(event.getData()).thenReturn(serializedData);

      // Jeu désérialisé simulé
      Game deserialized = new Game();
      deserialized.setName("Mocked game " + i);

      // Mock du désérialiseur
      when(gameDeserializer.deserialize("GAME_CREATED", serializedData)).thenReturn(deserialized);

      // Enqueue dans le service
      gameCreatedService.enqueue(event);

      // Appel du scheduler
      scheduler.handle();
    }

    Awaitility.await()
        .atMost(Duration.ofSeconds(30))
        .untilAsserted(() -> verify(gameRepository, times(3)).save(any(Game.class)));
  }
}
