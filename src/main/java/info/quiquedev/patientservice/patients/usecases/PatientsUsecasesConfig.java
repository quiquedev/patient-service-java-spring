package info.quiquedev.patientservice.patients.usecases;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientsUsecasesConfig {
  @Bean
  public PatientsUsecases patientsUsecases(final PatientsRepository repository, final Clock clock) {
    return new PatientsUsecases(repository, clock);
  }
}
