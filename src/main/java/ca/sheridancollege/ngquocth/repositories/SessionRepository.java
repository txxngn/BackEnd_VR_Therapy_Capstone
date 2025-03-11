package ca.sheridancollege.ngquocth.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

	List<Session> findByTherapist_UserName(String userName);
    
    List<Session> findByPatient_UserName(String userName);
    
}
