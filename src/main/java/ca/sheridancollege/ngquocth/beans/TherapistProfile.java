package ca.sheridancollege.ngquocth.beans;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@DiscriminatorValue("THERAPIST")
@EqualsAndHashCode(callSuper = true)
public class TherapistProfile extends User {

	
	@Column(nullable = true, unique = true)
    private String licenseNumber;

    @Column(nullable = true)
    private String specialization;

    @Column(nullable = true)
    private Integer experienceYears;
    
    
    
    @OneToMany(mappedBy = "therapist", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;
    //orphanRemoval ensure proper cleanup when removing related entities.
    
    @OneToMany(mappedBy = "createdBy", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Scenario> scenarios;
    
    @OneToMany(mappedBy = "therapist", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Customization> customizations;
    
    
}
