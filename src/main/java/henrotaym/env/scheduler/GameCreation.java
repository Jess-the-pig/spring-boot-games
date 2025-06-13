package henrotaym.env.scheduler;

import henrotaym.env.enums.ProfileName;
import henrotaym.env.queues.emitters.Emitter;
import henrotaym.env.queues.events.GameCreatedEvent;
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

  private final Emitter emitter;

  @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 30)
  public void handle() {

    log.info("Scheduler: Game creation runned");

    GameCreatedEvent event = new GameCreatedEvent("Créer une nouvelle game");
    emitter.send(event);

    log.info("Scheduler: GameCreatedEvent envoyé au topic Kafka");
  }
}
