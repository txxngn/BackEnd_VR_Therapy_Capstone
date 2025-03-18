package ca.sheridancollege.ngquocth.beans;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    
    private LocalDateTime sessionDate;
    private Integer sessionDuration; //duration in minutes
    private String scenarioUsed;
    private String feedback;
    
    
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    @JsonIgnore
    private TherapistProfile therapist;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private PatientProfile patient;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_tracker_id")
    @JsonIgnore
    private ProgressTracker progressTracker;
    
}
