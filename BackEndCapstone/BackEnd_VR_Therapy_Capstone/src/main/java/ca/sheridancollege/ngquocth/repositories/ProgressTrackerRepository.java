package ca.sheridancollege.ngquocth.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.sheridancollege.ngquocth.beans.ProgressTracker;

@Repository
public interface ProgressTrackerRepository extends JpaRepository<ProgressTracker, Long> {

	//to look up a tracker by the patient's userName.
	ProgressTracker findByPatient_UserName(String userName);
	
	Optional<ProgressTracker> findByPatient_Email(String email);
	
}
