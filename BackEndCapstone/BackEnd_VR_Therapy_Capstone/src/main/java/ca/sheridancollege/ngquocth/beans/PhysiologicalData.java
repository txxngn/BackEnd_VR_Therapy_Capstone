package ca.sheridancollege.ngquocth.beans;

import java.time.LocalDateTime;

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
public class PhysiologicalData {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;
    
    private Double heartRate;
    private Double anxietyScore;
    private LocalDateTime timestamp;
    
    
    
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;
    

    
    
}
