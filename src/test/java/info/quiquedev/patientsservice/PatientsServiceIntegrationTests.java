package info.quiquedev.patientsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.quiquedev.patientsservice.patients.usecases.WithDatabase;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class PatientsServiceIntegrationTests extends WithDatabase {
  @LocalServerPort int port;
  @Autowired TestRestTemplate testRestTemplate;
  @Autowired    ObjectMapper objectMapper;

  @Test
  void testPatientCreation() throws JsonProcessingException {
var response =     testRestTemplate.getForObject("http://localhost:" + port + "/patients", String.class);

    assertThat(toJsonNode(response)).isEqualTo(toJsonNode(PATIENT_JSON));
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
}
