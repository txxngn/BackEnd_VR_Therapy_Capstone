package ca.sheridancollege.ngquocth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.ProgressTracker;

@Repository
public interface ProgressTrackerRepository extends JpaRepository<ProgressTracker, Long> {

	ProgressTracker findByPatient_UserName(String userName);
	
}
