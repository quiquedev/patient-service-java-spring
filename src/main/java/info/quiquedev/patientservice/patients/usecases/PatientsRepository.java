package info.quiquedev.patientservice.patients.usecases;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

interface PatientsRepository extends JpaRepository<Patient, String> {
  boolean existsPatientByPassportNumber(String passportNumber);
}

@Entity
@Table(name = "patients")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
class Patient {
  @Id private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @Column(unique = true, nullable = false)
  private String passportNumber;

  @Column(nullable = false)
  private Instant createdAt;

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Patient patient = (Patient) o;
    return Objects.equals(name, patient.name)
        && Objects.equals(surname, patient.surname)
        && Objects.equals(passportNumber, patient.passportNumber)
        && Objects.equals(createdAt, patient.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, passportNumber, createdAt);
  }
}
