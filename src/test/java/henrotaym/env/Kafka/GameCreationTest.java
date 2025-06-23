package henrotaym.env.Kafka;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import henrotaym.env.ApplicationTest;
import henrotaym.env.database.factories.GameFactory;
import henrotaym.env.deserializer.GameDeserializer;
import henrotaym.env.entities.Game;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.repositories.GameRepository;
import henrotaym.env.services.GameCreatedService;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class GameCreationTest extends ApplicationTest {
  @Autowired private GameFactory gameFactory;
  @Mock private GameRepository gameRepository;

  @Mock private GameDeserializer gameDeserializer;

  @InjectMocks private GameCreatedService gameCreatedService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void processPendingGames_shouldDeserializeAndSaveGame() throws Exception {
    Game game = gameFactory.create();
    String serializedGame = null;

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      serializedGame = objectMapper.writeValueAsString(game);
    } catch (JsonProcessingException e) {
      fail("Erreur lors de la sérialisation du game : " + e.getMessage());
    }

    GameCreatedEvent event = mock(GameCreatedEvent.class);

    when(event.eventName()).thenReturn("GAME_CREATED");

    byte[] serializedData = serializedGame.getBytes(StandardCharsets.UTF_8);

    when(event.getData()).thenReturn(serializedData);

    // Ajout de l’event dans la queue interne du service (via méthode publique)
    gameCreatedService.enqueue(event);

    // Création d’un Game simulé (désérialisé)
    Game deserializedGame = new Game();
    deserializedGame.setName("My Game");

    // Mock du désérialiseur pour retourner le Game simulé
    when(gameDeserializer.deserialize("GAME_CREATED", serializedData)).thenReturn(deserializedGame);

    // WHEN
    gameCreatedService.processPendingGames();

    // THEN
    // Vérifie que save() a bien été appelé avec le game désérialisé
    verify(gameRepository, times(1)).save(deserializedGame);
  }
}
