package ca.sheridancollege.ngquocth.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.Scenario;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

	List<Scenario> findByCreatedBy_UserName(String userName);
	
}
