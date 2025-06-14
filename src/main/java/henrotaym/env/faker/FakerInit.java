package henrotaym.env.faker;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakerInit {

  @Bean
  public Faker faker() {
    return new Faker();
  }
}
