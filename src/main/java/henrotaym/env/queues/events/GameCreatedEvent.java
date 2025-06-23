package henrotaym.env.queues.events;

import henrotaym.env.enums.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GameCreatedEvent implements Event {
  public static final String EVENT_NAME = EventName.GAME_CREATED;
  private byte[] data;

  public byte[] getData() {
    return data;
  }

  @Override
  public String eventName() {
    return EVENT_NAME;
  }
}
