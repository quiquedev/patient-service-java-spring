package info.quiquedev.patientsservice.patients.usecases.dtos;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class NewPatientDto {
  @Length(min = 1, max = 50, message = "'name' length must be between {min} and {max}")
  private String name;

  @Length(min = 1, max = 150, message = "'surname' length must be between {min} and {max}")
  private String surname;

  @Length(min = 10, max = 10, message = "'passportNumber' length must be between {min} and {max}")
  private String passportNumber;
}
