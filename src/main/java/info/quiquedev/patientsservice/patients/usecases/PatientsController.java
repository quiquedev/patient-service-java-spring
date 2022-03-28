package info.quiquedev.patientsservice.patients.usecases;

import info.quiquedev.patientsservice.patients.usecases.dtos.NewPatientDto;
import info.quiquedev.patientsservice.patients.usecases.dtos.PatientDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController("/patients")
@RequiredArgsConstructor
public class PatientsController {
  private final PatientsUsecases usecases;

  @PostMapping
  public @ResponseBody  PatientDto createPatient(@Validated @RequestBody NewPatientDto newPatientDto)
      throws PassportNumberNotUniqueException {
    return usecases.createPatient(newPatientDto);
  }

  @GetMapping("/id")
  public @ResponseBody Optional<PatientDto> findPatientById(@PathVariable("id") final String id) {
    return usecases.findPatientById(id);
  }

  @ExceptionHandler(PassportNumberNotUniqueException.class)
  public ResponseEntity<ErrorDto> handlePassportNumberNotUniqueException(
      final PassportNumberNotUniqueException exception) {

    return new ResponseEntity<>(new ErrorDto(exception.getMessage()), BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleAnyOtherException(final Exception exception) {
    return new ResponseEntity<>(
        new ErrorDto("please try later or contact us"), INTERNAL_SERVER_ERROR);
  }
}

@Data
class ErrorDto {
  private final String message;
  private Set<String> errors;
}
