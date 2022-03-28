package info.quiquedev.patientsservice.patients.usecases;

import info.quiquedev.patientsservice.patients.usecases.dtos.NewPatientDto;
import info.quiquedev.patientsservice.patients.usecases.dtos.PatientDto;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class PatientsUsecases {
  private final PatientsRepository repository;
  private final Clock clock;

  PatientDto createPatient(final NewPatientDto newPatientDto)
      throws PassportNumberNotUniqueException {
    if (repository.existsPatientByPassportNumber(newPatientDto.getPassportNumber()))
      throw new PassportNumberNotUniqueException(newPatientDto.getPassportNumber());

    var patient =
        Patient.builder()
            .id(UUID.randomUUID().toString())
            .name(newPatientDto.getName())
            .surname(newPatientDto.getSurname())
            .passportNumber(newPatientDto.getPassportNumber())
            .createdAt(Instant.now(clock))
            .build();

    return toDto(repository.save(patient));
  }

  Optional<PatientDto> findPatientById(final String id) {
    return repository.findById(id).map(PatientsUsecases::toDto);
  }

  private static PatientDto toDto(final Patient saved) {
    return PatientDto.builder()
        .id(saved.getId())
        .name(saved.getName())
        .surname(saved.getSurname())
        .passportNumber(saved.getPassportNumber())
        .createdAt(saved.getCreatedAt())
        .build();
  }
}

class PassportNumberNotUniqueException extends Exception {
  PassportNumberNotUniqueException(final String passportNumber) {
    super(MessageFormat.format("passport number {0} non unique", passportNumber));
  }
}
