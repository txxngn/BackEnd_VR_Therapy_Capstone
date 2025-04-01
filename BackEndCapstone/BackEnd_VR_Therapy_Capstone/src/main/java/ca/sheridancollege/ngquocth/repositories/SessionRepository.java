package ca.sheridancollege.ngquocth.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.PatientProfile;
import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.beans.TherapistProfile;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

	//find sessions by patient
    List<Session> findByPatient(PatientProfile patient);

    //find sessions by therapist
    List<Session> findByTherapist(TherapistProfile therapist);
    
    
    //find sessions within a time range
    List<Session> findBySessionDateBetween(LocalDateTime start, LocalDateTime end);
}
