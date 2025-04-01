package ca.sheridancollege.ngquocth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.ngquocth.beans.PatientProfile;
import ca.sheridancollege.ngquocth.beans.ProgressTracker;
import ca.sheridancollege.ngquocth.beans.Role;
import ca.sheridancollege.ngquocth.beans.TherapistProfile;
import ca.sheridancollege.ngquocth.beans.User;
import ca.sheridancollege.ngquocth.models.AuthenticationRequest;
import ca.sheridancollege.ngquocth.models.AuthenticationResponse;
import ca.sheridancollege.ngquocth.repositories.PatientProfileRepository;
import ca.sheridancollege.ngquocth.repositories.ProgressTrackerRepository;
import ca.sheridancollege.ngquocth.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

	
	//this class handle user registration and authentication
	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    
    private final ProgressTrackerRepository progressTrackerRepo;
    private final PatientProfileRepository patientRepo;

    //register a new user
    public AuthenticationResponse register(User user) {
    	//automatically assign role based on userType
        if (user instanceof TherapistProfile) {
            ((TherapistProfile) user).setRole(Role.THERAPIST);
        } else if (user instanceof PatientProfile) {
            ((PatientProfile) user).setRole(Role.PATIENT);
        }
    	
    	
    	
    	
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        //Create tracker if this is a patient
        if (savedUser instanceof PatientProfile) {
            PatientProfile patient = (PatientProfile) savedUser;

            ProgressTracker tracker = ProgressTracker.builder()
                .patient(patient)
                .improvementScore(0.0)
                .build();

            progressTrackerRepo.save(tracker);
            patient.setProgressTracker(tracker);
            patientRepo.save(patient);
        }

        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwtToken);
    }

    //authenticate an existing user
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
    
    
    //add login
    public AuthenticationResponse login(AuthenticationRequest request) {
        // Fetch the user by username or email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Validate the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        // Return the response with the token
        return new AuthenticationResponse(jwtToken);
    }

    
}
