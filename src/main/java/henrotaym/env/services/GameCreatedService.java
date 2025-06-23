package henrotaym.env.services;

import henrotaym.env.deserializer.GameDeserializer;
import henrotaym.env.entities.Game;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.repositories.GameRepository;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameCreatedService {
  private final Queue<GameCreatedEvent> pendingGames = new ConcurrentLinkedQueue<>();
  private GameRepository gameRepository;
  private GameDeserializer gameDeserializer;

  public void enqueue(GameCreatedEvent game) {
    log.info("Ajout du jeu à la file d'attente : {}", game);
    pendingGames.offer(game);
  }

  public void processPendingGames() {
    GameCreatedEvent event;

    while ((event = pendingGames.poll()) != null) {
      try {
        Game game =
            gameDeserializer.deserialize(
                event.eventName(), event.getData()); // adapte si besoin le 2e argument
        log.info("Traitement du jeu désérialisé : {}", game);
        gameRepository.save(game);
      } catch (Exception e) {
        log.error("Erreur lors de la désérialisation ou du traitement du jeu", e);
      }
    }
  }
}
