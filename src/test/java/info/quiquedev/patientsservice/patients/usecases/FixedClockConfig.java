package info.quiquedev.patientsservice.patients.usecases;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class FixedClockConfig {
  static final Clock FIXED_CLOCK =
      Clock.fixed(Instant.ofEpochMilli(1607609508000l), ZoneId.of("Europe/Berlin"));

  @Bean
  @Primary
  Clock fixedClock() {
    return FIXED_CLOCK;
  }
}
