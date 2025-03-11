package ca.sheridancollege.ngquocth.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customization {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customizationId;
    
	private String changesDescription;
	
	
	
	
    @ManyToOne(fetch = FetchType.LAZY) //optimize performance for large data relationships
    @JoinColumn(name = "therapist_id", nullable = false)
    private TherapistProfile therapist;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;
    
    
	
	
	
}
