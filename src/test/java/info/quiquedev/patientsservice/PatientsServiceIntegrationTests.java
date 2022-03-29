package info.quiquedev.patientsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.quiquedev.patientsservice.patients.FixedClockConfig;
import info.quiquedev.patientsservice.patients.WithDatabase;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(FixedClockConfig.class)
class PatientsServiceIntegrationTests extends WithDatabase {
  @LocalServerPort int port;
  @Autowired TestRestTemplate testRestTemplate;
  @Autowired  ObjectMapper objectMapper;

  @Test
  void testPatientCreation() throws JsonProcessingException, URISyntaxException {
    var uri = new URI("http://localhost:" + port + "/patients");
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);
    var request = new HttpEntity<>(NEW_PATIENT_JSON, headers);
    var response = testRestTemplate.postForEntity(uri, request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(toJsonNode(response.getBody())).isEqualTo(toJsonNode(PATIENT_JSON));
  }

  private JsonNode toJsonNode(final String value) throws JsonProcessingException {
   return objectMapper.readTree(value) ;
  }

  private static final String PATIENT_JSON = """
          {
            "id": "id",
            "name": "enrique",
            "surname": "molina",
            "passportNumber": "123458760X",
            "createdAt": "2020-12-10T14:11:48Z"
          }
          """.stripIndent();
  private static final String NEW_PATIENT_JSON = """
          {
            "name": "enrique",
            "surname": "molina",
            "passportNumber": "123458760X"
          }
          """.stripIndent();
}
