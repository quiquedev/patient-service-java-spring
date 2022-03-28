package info.quiquedev.patientsservice.patients.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PatientsUsecasesConfig {
  @Bean
  public PatientsUsecases patientsUsecases(
      final PatientsRepository repository, final Clock clock) {
    return new PatientsUsecases(repository, clock);
  }
}
