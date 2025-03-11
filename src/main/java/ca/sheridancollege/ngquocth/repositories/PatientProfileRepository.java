package ca.sheridancollege.ngquocth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.PatientProfile;


@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {


    PatientProfile findByUserName(String userName);
	
}
