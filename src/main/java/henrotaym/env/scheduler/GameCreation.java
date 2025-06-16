package henrotaym.env.scheduler;

import henrotaym.env.enums.ProfileName;
import henrotaym.env.http.requests.GameRequest;
import henrotaym.env.services.GameService;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@Profile(ProfileName.SCHEDULER)
@Service
@RequiredArgsConstructor
public class GameCreation {

  private final Queue<GameRequest> queue = new ConcurrentLinkedQueue<>();
  private final GameService gameService;

  public void addEvent(GameRequest event) {
    queue.add(event);
    log.info("✅ Événement ajouté à la file : {}", event.toString());
  }

  @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 10)
  public void handle() {
    log.info("Scheduler: Game creation runned");

    GameRequest event;
    while ((event = queue.poll()) != null) {
      // 💥 Traitement métier

      gameService.store(event);

      log.info("✅ Game créé à partir de l'event : {}", event.toString());
    }
  }
}
