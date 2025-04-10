package ca.sheridancollege.ngquocth.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder //Use SuperBuilder because Lombok's @Builder doesn't support abstract classes directly.
@DiscriminatorValue("PATIENT")
@EqualsAndHashCode(callSuper = true)
public class PatientProfile extends User {

	@Column(nullable = true)
    private Double anxietyLevel;

    @Column(nullable = true)
    private Double heartRate;

    @Column(nullable = true)
    private String therapyGoal;

	
    @PrePersist
    private void assignRole() {
        this.role = Role.PATIENT;
    }
 
    
    @OneToMany(mappedBy = "patient", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Session> sessions;
    
    @OneToMany(mappedBy = "patient", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PhysiologicalData> physiologicalData;
    
    @OneToOne(mappedBy = "patient", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private ProgressTracker progressTracker;
    
    
    
    /*
     * Explain @EqualsAndHashCode(callSuper = true)
     * @Data automatically generates equals(), hashCode(), toString(), getters, and setters.
     * Both TherapistProfile and PatientProfile extend the User superclass, 
     * but Lombok's @Data by default DOES NOT include superclass fields 
     * in the generating above methods.
     * -->This @EqualsAndHashCode(callSuper = true) is suggested from the lombok itself.
     */
    
    
}
