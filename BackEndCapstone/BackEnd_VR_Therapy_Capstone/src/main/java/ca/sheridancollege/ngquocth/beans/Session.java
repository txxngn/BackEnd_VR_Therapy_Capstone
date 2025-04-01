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
    
    //reminder flags
    @Builder.Default
    private boolean isReminderSent1Hour = false;
    
    @Builder.Default
    private boolean isReminderSent24Hour = false;
    
    
    
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
    
    
    //for progress tracker score
    public int getFeedbackLength() {
        return feedback != null ? feedback.trim().length() : 0;
    }

    
 // Implemented getters because @JsonIgnore hides therapist and patient data to avoid infinite loop
    public String getTherapistName() {
        return therapist != null ? therapist.getFullName() : "N/A";
    }

    public String getTherapistLicense() {
        return therapist != null ? therapist.getLicenseNumber() : "N/A";
    }

    public String getPatientName() {
        return patient != null ? patient.getFullName() : "N/A";
    }

    public String getPatientGoal() {
        return patient != null ? patient.getTherapyGoal() : "N/A";
    }
    
    
    

    
}
