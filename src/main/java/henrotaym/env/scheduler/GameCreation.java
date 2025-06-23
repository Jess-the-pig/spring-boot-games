package henrotaym.env.scheduler;

import henrotaym.env.enums.ProfileName;
import henrotaym.env.services.GameCreatedService;
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

  private final GameCreatedService gameCreatedService;

  @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 30, fixedRate = 30)
  public void handle() {
    gameCreatedService.processPendingGames();

    log.info("Scheduler: Game creation runned");
  }
}
