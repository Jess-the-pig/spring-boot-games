package henrotaym.env.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import henrotaym.env.ApplicationTest;
import henrotaym.env.database.factories.CoverFactory;
import henrotaym.env.database.factories.GameFactory;
import henrotaym.env.database.factories.StudioFactory;
import henrotaym.env.database.factories.TagFactory;
import henrotaym.env.entities.Cover;
import henrotaym.env.entities.Game;
import henrotaym.env.entities.Studio;
import henrotaym.env.entities.Tag;
import henrotaym.env.enums.EventName;
import henrotaym.env.enums.ProfileName;
import henrotaym.env.http.requests.GameRequest;
import henrotaym.env.http.requests.relationships.CoverRelationshipRequest;
import henrotaym.env.http.requests.relationships.StudioRelationshipRequest;
import henrotaym.env.http.requests.relationships.TagRelationshipRequest;
import henrotaym.env.repositories.GameRepository;
import henrotaym.env.serializers.GameSerializer;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@EmbeddedKafka(
    partitions = 1,
    topics = {"game-created"},
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
@Slf4j
@ActiveProfiles(ProfileName.SCHEDULER)
public class GameKafkaIntegrationTest extends ApplicationTest {
  @Autowired private GameRepository gameRepository;
  @Autowired private KafkaTemplate<EventName, GameRequest> kafkaTemplate;
  @Autowired GameSerializer gameSerializer;
  @Autowired CoverFactory coverFactory;
  @Autowired StudioFactory studioFactory;
  @Autowired TagFactory tagFactory;
  @Autowired GameFactory gameFactory;

  private final ObjectMapper objectMapper =
      new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  @Test
  void should_send_5_random_game_creation_events() throws InterruptedException {

    for (int i = 1; i <= 5; i++) {

      Cover cover = this.coverFactory.create();
      Studio studio = this.studioFactory.create();
      Tag tag = this.tagFactory.create();
      String name = "Mon super jeu";

      CoverRelationshipRequest coverRel = new CoverRelationshipRequest(cover.getId());
      StudioRelationshipRequest studioRel = new StudioRelationshipRequest(studio.getId());
      List<TagRelationshipRequest> tagRels = List.of(new TagRelationshipRequest(tag.getId()));

      GameRequest gameRequest = new GameRequest(name, null, null, null);
      kafkaTemplate.send(new ProducerRecord<>(EventName.GAME_CREATED, gameRequest));
    }

    // Attendre que les events soient consommés et traités
    Awaitility.await()
        .atMost(Duration.ofSeconds(200))
        .until(() -> gameRepository.findAll().size() >= 5);

    // Ensuite seulement, récupérer les jeux en base
    List<Game> games = gameRepository.findAll();

    try {
      String prettyGames = objectMapper.writeValueAsString(games);
      log.info("🎮 Liste des jeux créés:\n{}", prettyGames);
    } catch (JsonProcessingException e) {
      log.error("❌ Erreur lors de la sérialisation JSON des jeux", e);
    }

    // Vérifications
    assertThat(games).isNotEmpty().hasSizeGreaterThanOrEqualTo(5);
    games.forEach(game -> assertThat(game.getName()).isNotBlank());
  }
}
