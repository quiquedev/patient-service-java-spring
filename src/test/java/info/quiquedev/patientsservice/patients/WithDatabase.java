package info.quiquedev.patientsservice.patients;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class WithDatabase {
  @Container
  static PostgreSQLContainer<?> dbContainer =
      new PostgreSQLContainer<>("postgres:12")
          .withUsername("testcontainers")
          .withPassword("testcontainers")
          .withDatabaseName("tescontainers");

  @DynamicPropertySource
  static void datasourceConfig(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
    registry.add("spring.datasource.hikari.password", dbContainer::getPassword);
    registry.add("spring.datasource.hikari.username", dbContainer::getUsername);
    registry.add("spring.datasource.initialization-mode", () -> "always");
  }
}
