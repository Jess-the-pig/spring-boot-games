package henrotaym.env.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import henrotaym.env.entities.Game;
import org.apache.kafka.common.serialization.Deserializer;

public class GameDeserializer implements Deserializer<Game> {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Game deserialize(String topic, byte[] data) {
    try {
      if (data == null || data.length == 0) {
        return null;
      }
      return objectMapper.readValue(data, Game.class);
    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de la désérialisation du message Kafka en Game", e);
    }
  }
}
