package ca.sheridancollege.ngquocth.beans;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)		//if do as normal, there will be error "Detached Entity Passed to Persist", mean entity (like User) is already persisted but is being passed again to be persisted by another entity	
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)  //So we'll use Inheritance approach, @DiscriminatorColumn wll handle field "userType" so i commented it
@Table(name = "users")  //change table name to avoid reserved keyword conflict (User) in SQL databases, including H2 
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder //have to use SuperBuilder because Lombok's @Builder doesn't support abstract classes directly.
public abstract class User {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;
	
	
	
	/*
	@OneToOne (mappedBy="user", cascade = jakarta.persistence.CascadeType.ALL)
	private PatientProfile patientProfile;
	
	@OneToOne (mappedBy="user", cascade = jakarta.persistence.CascadeType.ALL)
	private TherapistProfile therapistProfile;
	*/
	

}
