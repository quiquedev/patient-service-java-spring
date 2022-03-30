package info.quiquedev.patientsservice;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PatientsServiceApplication {
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  public static void main(String[] args) {
    SpringApplication.run(PatientsServiceApplication.class, args);
  }
}
