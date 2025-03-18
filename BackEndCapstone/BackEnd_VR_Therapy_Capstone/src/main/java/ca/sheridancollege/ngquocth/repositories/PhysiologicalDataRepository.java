package ca.sheridancollege.ngquocth.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.PhysiologicalData;

@Repository
public interface PhysiologicalDataRepository extends JpaRepository<PhysiologicalData, Long> {

	List<PhysiologicalData> findByPatient_UserName(String userName);
	
}
