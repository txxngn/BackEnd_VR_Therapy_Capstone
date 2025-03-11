package ca.sheridancollege.ngquocth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.TherapistProfile;

@Repository
public interface TherapistProfileRepository extends JpaRepository<TherapistProfile, Long> {
	
	//User is an abstract superclass. Access its data via TherapistProfile or PatientProfile.
	//so no User repository
    TherapistProfile findByLicenseNumber(String licenseNumber);
    
    TherapistProfile findByUserName(String userName);

}
