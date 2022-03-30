package info.quiquedev.patientservice.patients.usecases;

import static info.quiquedev.patientservice.patients.usecases.FixedClockConfig.FIXED_CLOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import info.quiquedev.patientservice.patients.usecases.dtos.NewPatientDto;
import info.quiquedev.patientservice.patients.usecases.dtos.PatientDto;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatientsUsecasesTest {
  final PatientsRepository repository = mock(PatientsRepository.class);

  PatientsUsecases usecases;

  @BeforeEach
  public void prepareTest() {
    reset(repository);
    usecases = new PatientsUsecases(repository, FIXED_CLOCK);
  }

  @Test
  public void testCreatePatient() throws PassportNumberNotUniqueException {
    // given
    when(repository.save(PATIENT)).thenReturn(PATIENT);

    // when
    var patient = usecases.createPatient(NEW_PATIENT_DTO);

    // then
    assertThat(patient).isEqualTo(PATIENT_DTO);
  }

  @Test
  public void testCreatePatientShouldFailIfPassportNumberNotUnique() {
    // given
    when(repository.existsPatientByPassportNumber(NEW_PATIENT_DTO.getPassportNumber()))
        .thenReturn(true);

    // when
    assertThatCode(() -> usecases.createPatient(NEW_PATIENT_DTO))
        .isInstanceOf(PassportNumberNotUniqueException.class);
  }

  public void testFindPatientById() {
    // given
    when(repository.findById(PATIENT.getId())).thenReturn(Optional.of(PATIENT));

    // when
    var patient = usecases.findPatientById(PATIENT.getId());

    // when
    assertThat(patient).contains(PATIENT_DTO);
  }

  public void testFindPatientByIdIfNotFound() {
    // given
    when(repository.findById(PATIENT.getId())).thenReturn(Optional.empty());

    // when
    var patient = usecases.findPatientById(PATIENT.getId());

    // when
    assertThat(patient).isEmpty();
  }

  private static final NewPatientDto NEW_PATIENT_DTO =
      NewPatientDto.builder()
          .name("enrique")
          .surname("molina")
          .passportNumber("123458760X")
          .build();
  private static final Patient PATIENT =
      Patient.builder()
          .id("id")
          .name(NEW_PATIENT_DTO.getName())
          .surname(NEW_PATIENT_DTO.getSurname())
          .passportNumber(NEW_PATIENT_DTO.getPassportNumber())
          .createdAt(Instant.now(FIXED_CLOCK))
          .build();
  private static final PatientDto PATIENT_DTO =
      PatientDto.builder()
          .id(PATIENT.getId())
          .name(PATIENT.getName())
          .surname(PATIENT.getSurname())
          .passportNumber(PATIENT.getPassportNumber())
          .createdAt(PATIENT.getCreatedAt())
          .build();
}
