package henrotaym.env.queues.events;

import henrotaym.env.enums.EventName;
import henrotaym.env.http.requests.GameRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameCreatedEvent implements Event {
  private GameRequest message;
  public static final String EVENT_NAME = EventName.GAME_CREATED;

  @Override
  public String eventName() {
    return EVENT_NAME;
  }

  public GameRequest gamerequest() {
    return message;
  }
}
