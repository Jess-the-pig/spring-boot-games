package henrotaym.env.queues.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private String message;
  public static final String EVENT_NAME = EventName.GAME_CREATED;

  @Override
  public String eventName() {
    return EVENT_NAME;
  }

  public GameRequest gameinfo() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(message, GameRequest.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid message format", e);
    }
  }
}
