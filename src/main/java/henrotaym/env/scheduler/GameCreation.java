package henrotaym.env.scheduler;

import henrotaym.env.enums.ProfileName;
import henrotaym.env.http.requests.GameRequest;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.services.GameService;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(ProfileName.SCHEDULER)
@RequiredArgsConstructor
public class GameCreation {

  private final GameService gameService;
  private final Queue<GameCreatedEvent> queue = new ConcurrentLinkedQueue<>();

  public void addEvent(GameCreatedEvent event) {
    queue.add(event);
    log.info("✅ Événement ajouté à la file : {}", event.toString());
  }

  @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 10)
  public void handle() {
    log.info("Scheduler: Game creation runned");
    GameCreatedEvent event;
    while ((event = queue.poll()) != null) {
      GameRequest gameInfo = event.gameinfo();
      gameService.store(gameInfo);
      log.info("Je suis dans la queue" + event);
      log.info(gameInfo.toString());
    }
  }
}
