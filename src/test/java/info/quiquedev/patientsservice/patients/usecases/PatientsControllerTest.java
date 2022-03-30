package info.quiquedev.patientsservice.patients.usecases;

import static info.quiquedev.patientsservice.patients.usecases.FixedClockConfig.FIXED_CLOCK;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import info.quiquedev.patientsservice.patients.usecases.dtos.NewPatientDto;
import info.quiquedev.patientsservice.patients.usecases.dtos.PatientDto;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PatientsController.class)
class PatientsControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private PatientsUsecases patientsUsecases;

  @Test
  public void testCreatePatient() throws Exception {
    when(patientsUsecases.createPatient(NEW_PATIENT_DTO)).thenReturn(PATIENT_DTO);

    mockMvc
        .perform(post("/patients").content(NEW_PATIENT_JSON).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(PATIENT_DTO.getId()))
        .andExpect(jsonPath("$.name").value(PATIENT_DTO.getName()))
        .andExpect(jsonPath("$.surname").value(PATIENT_DTO.getSurname()))
        .andExpect(jsonPath("$.passportNumber").value(PATIENT_DTO.getPassportNumber()))
        .andExpect(jsonPath("$.createdAt").value(PATIENT_DTO.getCreatedAt().toString()));
  }

  @Test
  public void testCreatePatientWithPassportNotUnique() throws Exception {
    when(patientsUsecases.createPatient(NEW_PATIENT_DTO))
        .thenThrow(PassportNumberNotUniqueException.class);

    mockMvc
        .perform(post("/patients").content(NEW_PATIENT_JSON).contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testFindPatientById() throws Exception {
    when(patientsUsecases.findPatientById(PATIENT_DTO.getId()))
        .thenReturn(Optional.of(PATIENT_DTO));

    mockMvc
        .perform(get("/patients/" + PATIENT_DTO.getId()))
        .andExpect(status().isOk())
        .andExpect(content().json(PATIENT_JSON));
  }

  @Test
  public void testFindPatientByIdNotFound() throws Exception {
    when(patientsUsecases.findPatientById(PATIENT_DTO.getId())).thenReturn(Optional.empty());

    mockMvc.perform(get("/patients/" + PATIENT_DTO.getId())).andExpect(status().isNotFound());
  }

  private static final NewPatientDto NEW_PATIENT_DTO =
      NewPatientDto.builder()
          .name("enrique")
          .surname("molina")
          .passportNumber("123458760X")
          .build();
  private static final PatientDto PATIENT_DTO =
      PatientDto.builder()
          .id("id")
          .name(NEW_PATIENT_DTO.getName())
          .surname(NEW_PATIENT_DTO.getSurname())
          .passportNumber(NEW_PATIENT_DTO.getPassportNumber())
          .createdAt(Instant.now(FIXED_CLOCK))
          .build();
  private static final String NEW_PATIENT_JSON =
      """
          {
            "name": "enrique",
            "surname": "molina",
            "passportNumber": "123458760X"
          }
          """
          .stripIndent();
  private static final String PATIENT_JSON =
      """
          {
            "id": "id",
            "name": "enrique",
            "surname": "molina",
            "passportNumber": "123458760X",
            "createdAt": "2020-12-10T14:11:48Z"
          }
          """
          .stripIndent();
}
