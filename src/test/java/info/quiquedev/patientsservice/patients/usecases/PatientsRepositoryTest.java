package info.quiquedev.patientsservice.patients.usecases;

import static info.quiquedev.patientsservice.patients.usecases.FixedClockConfig.FIXED_CLOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
class PatientsRepositoryTest extends WithDatabase {
  @Autowired private PatientsRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void testPatientCreation() {
    // given
    var patient =
        Patient.builder()
            .id("id")
            .name("enrique")
            .surname("molina")
            .passportNumber("123456789X")
            .createdAt(Instant.now(FIXED_CLOCK))
            .build();

    // when
    repository.save(patient);

    // then
    var saved = repository.findById(patient.getId());
    assertThat(saved).contains(patient);
  }

  @Test
  void testPatientCreationShouldFailIfPassportNumberIsNotUnique() {
    // given
    var patient1 =
        Patient.builder()
            .id("id-1")
            .name("enrique")
            .surname("molina")
            .passportNumber("123456789X")
            .createdAt(Instant.now(FIXED_CLOCK))
            .build();
    repository.save(patient1);

    var patient2 =
        Patient.builder()
            .id("id-2")
            .name("enrique")
            .surname("molina")
            .passportNumber("123456789X")
            .createdAt(Instant.now(FIXED_CLOCK))
            .build();

    // when
    assertThatCode(() -> repository.save(patient2))
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void testExistsPatientByPassportNumber() {
    // given
    var patient =
        Patient.builder()
            .id("id-1")
            .name("enrique")
            .surname("molina")
            .passportNumber("123456789X")
            .createdAt(Instant.now(FIXED_CLOCK))
            .build();
    repository.save(patient);

    // when
    var exists = repository.existsPatientByPassportNumber(patient.getPassportNumber());

    // then
    assertThat(exists).isTrue();
  }

  @Test
  void testExistsPatientByPassportNumberNotFound() {
    // when
    var exists = repository.existsPatientByPassportNumber("1234");

    // then
    assertThat(exists).isFalse();
  }
}
