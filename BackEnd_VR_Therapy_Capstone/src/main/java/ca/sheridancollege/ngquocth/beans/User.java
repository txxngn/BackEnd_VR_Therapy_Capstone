package ca.sheridancollege.ngquocth.beans;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.EXISTING_PROPERTY, 
	    property = "userType"
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = TherapistProfile.class, name = "THERAPIST"),
	    @JsonSubTypes.Type(value = PatientProfile.class, name = "PATIENT")
	})
public abstract class User implements UserDetails{

	
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
	
	
	
    
    
    //for security
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
    
    
    

    //UserDetails Methods
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email; // Use email for authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify if account expiration is needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify if account lock logic is needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify if credential expiration is needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Modify if you need to disable accounts
    }

}
