package info.quiquedev.patientsservice.patients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class FixedClockConfig {
  public static final Clock FIXED_CLOCK =
      Clock.fixed(Instant.ofEpochMilli(1607609508000l), ZoneId.of("Europe/Berlin"));

  @Bean
  @Primary
  Clock fixedClock() {
    return FIXED_CLOCK;
  }
}
