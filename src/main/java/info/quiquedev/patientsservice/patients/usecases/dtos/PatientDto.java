package info.quiquedev.patientsservice.patients.usecases.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

@Data
@Builder
public class PatientDto {
  private final String id;
  private final String name;
  private final String surname;
  private final String passportNumber;
  private final Instant createdAt;
}
