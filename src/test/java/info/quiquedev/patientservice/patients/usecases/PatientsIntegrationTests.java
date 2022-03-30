package info.quiquedev.patientservice.patients.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(FixedClockConfig.class)
class PatientsIntegrationTests extends WithDatabase {
  @LocalServerPort int port;
  @Autowired TestRestTemplate testRestTemplate;
  @Autowired PatientsRepository patientsRepository;
  @Autowired Clock clock;

  @BeforeEach
  void prepareTest() {
    patientsRepository.deleteAll();
  }

  @Test
  void testPatientCreation() throws JsonProcessingException, URISyntaxException {
    // given
    var uri = new URI("http://localhost:" + port + "/patients");
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    var request = new HttpEntity<>(NEW_PATIENT_JSON, headers);

    // when
    var response = testRestTemplate.postForEntity(uri, request, String.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);

    var body = JsonPath.parse(response.getBody());

    assertThat(body.read("$.id", String.class)).matches(UUID_PATTERN);
    assertThat(body.read("$.name", String.class)).isEqualTo("enrique");
    assertThat(body.read("$.surname", String.class)).isEqualTo("molina");
    assertThat(body.read("$.passportNumber", String.class)).isEqualTo("123458760X");
    assertThat(body.read("$.createdAt", String.class)).isEqualTo("2020-12-10T14:11:48Z");
  }

  @Test
  void testFindPatientById() throws URISyntaxException {
    // given
    var patient =
        Patient.builder()
            .id("id1")
            .name("enrique")
            .surname("molina")
            .passportNumber("123458760X")
            .createdAt(Instant.now(clock))
            .build();
    patientsRepository.save(patient);

    var uri = new URI("http://localhost:" + port + "/patients/id1");

    // when
    var response = testRestTemplate.getForEntity(uri, String.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    var body = JsonPath.parse(response.getBody());

    assertThat(body.read("$.id", String.class)).isEqualTo("id1");
    assertThat(body.read("$.name", String.class)).isEqualTo("enrique");
    assertThat(body.read("$.surname", String.class)).isEqualTo("molina");
    assertThat(body.read("$.passportNumber", String.class)).isEqualTo("123458760X");
    assertThat(body.read("$.createdAt", String.class)).isEqualTo("2020-12-10T14:11:48Z");
  }

  private static final String UUID_PATTERN =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

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
  private static final String NEW_PATIENT_JSON =
      """
          {
            "name": "enrique",
            "surname": "molina",
            "passportNumber": "123458760X"
          }
          """
          .stripIndent();
}
