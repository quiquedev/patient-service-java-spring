package info.quiquedev.patientsservice.patients.usecases;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import com.fasterxml.jackson.annotation.JsonInclude;
import info.quiquedev.patientsservice.patients.usecases.dtos.NewPatientDto;
import info.quiquedev.patientsservice.patients.usecases.dtos.PatientDto;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientsController {
  private final PatientsUsecases usecases;

  @PostMapping
  public ResponseEntity<?> createPatient(
      @Validated @RequestBody NewPatientDto newPatientDto, Errors errors)
      throws PassportNumberNotUniqueException {
    if (errors.hasErrors()) {
      var errorDto = createErrorDto(errors);
      return badRequest().body(errorDto);
    } else return ok(usecases.createPatient(newPatientDto));
  }

  private ErrorDto createErrorDto(final Errors errors) {
    var errorMessages =
        errors.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableSet());
    var errorDto = new ErrorDto("invalid body", errorMessages);
    return errorDto;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PatientDto> findPatientById(@PathVariable("id") final String id) {
    return usecases.findPatientById(id).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
  }

  @ExceptionHandler(PassportNumberNotUniqueException.class)
  public ResponseEntity<ErrorDto> handlePassportNumberNotUniqueException(
      final PassportNumberNotUniqueException exception) {

    return new ResponseEntity<>(new ErrorDto(exception.getMessage()), BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleValidationException(
      final MethodArgumentNotValidException exception) {
    return new ResponseEntity<>(
        new ErrorDto("please try later or contact us"), INTERNAL_SERVER_ERROR);
  }
}

@Data
@AllArgsConstructor
@RequiredArgsConstructor
class ErrorDto {
  private final String message;

  @JsonInclude(NON_NULL)
  private Set<String> errors;
}
