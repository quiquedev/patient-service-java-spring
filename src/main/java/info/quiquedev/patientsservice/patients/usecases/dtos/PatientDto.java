package info.quiquedev.patientsservice.patients.usecases.dtos;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDto {
  private final String id;
  private final String name;
  private final String surname;
  private final String passportNumber;
  private final Instant createdAt;
}
