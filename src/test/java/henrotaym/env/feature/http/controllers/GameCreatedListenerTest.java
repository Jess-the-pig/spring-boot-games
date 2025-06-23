/*package henrotaym.env.feature.http.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import henrotaym.env.ApplicationTest;
import henrotaym.env.queues.events.GameCreatedEvent;
import henrotaym.env.queues.listeners.GameCreatedListener;
import henrotaym.env.scheduler.GameCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameCreatedListenerTest extends ApplicationTest {

  private GameCreation gameCreation;
  private GameCreatedListener listener;

  @BeforeEach
  void setUp() {
    gameCreation = mock(GameCreation.class);
    listener = new GameCreatedListener(gameCreation);
  }

  @Test
  void listen_shouldAddEventToGameCreationAndRecordMessage() {
    GameCreatedEvent event = new GameCreatedEvent("message");
    listener.listen(event);

    verify(gameCreation, times(1)).addEvent(event);
    assertTrue(listener.getReceivedMessages().contains(event.toString()));
  }
}*/
