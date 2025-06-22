package henrotaym.env.feature.http.controllers;

import henrotaym.env.ApplicationTest;
import henrotaym.env.enums.EventName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

class KafkaTemplateTest extends ApplicationTest {

  @Autowired private EventName eventName;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  void listen_shouldAddEventToGameCreationAndRecordMessage() {}
}
