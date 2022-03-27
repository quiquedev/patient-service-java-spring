package info.quiquedev.patientsservice.patients.usecases;

import info.quiquedev.patientsservice.patients.usecases.repository.PatientsRepository;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
class PatientsRepositoryTest extends WithDatabase {
  @Autowired private PatientsRepository repository;

  @BeforeEach
  void setUp() {

  }

  @Test
  void test() {

  }
}
