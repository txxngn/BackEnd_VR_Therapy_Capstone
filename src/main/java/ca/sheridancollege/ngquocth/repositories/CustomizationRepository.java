package ca.sheridancollege.ngquocth.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.Customization;

@Repository
public interface CustomizationRepository extends JpaRepository<Customization, Long> {

	List<Customization> findByTherapist_UserName(String userName);
    
    List<Customization> findByScenario_ScenarioId(Long scenarioId);
    
}
