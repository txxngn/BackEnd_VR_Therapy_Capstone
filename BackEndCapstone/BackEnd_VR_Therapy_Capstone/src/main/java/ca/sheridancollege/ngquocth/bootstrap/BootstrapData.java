package ca.sheridancollege.ngquocth.bootstrap;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ca.sheridancollege.ngquocth.beans.Customization;
import ca.sheridancollege.ngquocth.beans.PatientProfile;
import ca.sheridancollege.ngquocth.beans.PhysiologicalData;
import ca.sheridancollege.ngquocth.beans.ProgressTracker;
import ca.sheridancollege.ngquocth.beans.Role;
import ca.sheridancollege.ngquocth.beans.Scenario;
import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.beans.TherapistProfile;
import ca.sheridancollege.ngquocth.repositories.CustomizationRepository;
import ca.sheridancollege.ngquocth.repositories.PatientProfileRepository;
import ca.sheridancollege.ngquocth.repositories.PhysiologicalDataRepository;
import ca.sheridancollege.ngquocth.repositories.ProgressTrackerRepository;
import ca.sheridancollege.ngquocth.repositories.ScenarioRepository;
import ca.sheridancollege.ngquocth.repositories.SessionRepository;
import ca.sheridancollege.ngquocth.repositories.TherapistProfileRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private TherapistProfileRepository therapistRepo;
    private PatientProfileRepository patientRepo;
    private ScenarioRepository scenarioRepo;
    private CustomizationRepository customizationRepo;
    private SessionRepository sessionRepo;
    private ProgressTrackerRepository progressTrackerRepo;
    private PhysiologicalDataRepository physiologicalDataRepo;
    
    private final PasswordEncoder passwordEncoder; //for login test cuz spring security expect BCrypt-encoded passwords, not raw password

    @Override
    public void run(String... args) throws Exception {

    	// === 1. Create Therapists ===
        TherapistProfile therapist1 = TherapistProfile.builder()
                .fullName("Dr. John Smith")
                .email("john@example.com")
                .userName("johnsmith")
                .password(passwordEncoder.encode("password123"))
                .dateOfBirth(LocalDate.of(1980, 5, 10))
                .gender("Male")
                .licenseNumber("T1234")
                .specialization("Anxiety Management")
                .experienceYears(10)
                .build();

        TherapistProfile therapist2 = TherapistProfile.builder()
                .fullName("Dr. Emily Davis")
                .email("emily@example.com")
                .userName("emilydavis")
                .password(passwordEncoder.encode("password123"))
                .dateOfBirth(LocalDate.of(1985, 7, 20))
                .gender("Female")
                .licenseNumber("T5678")
                .specialization("Stress Relief")
                .experienceYears(7)
                .build();

        therapistRepo.saveAll(Arrays.asList(therapist1, therapist2));

        // === 2. Create Patients ===
        PatientProfile patient1 = PatientProfile.builder()
                .fullName("Alice Johnson")
                .email("alice@example.com")
                .userName("alicejohnson")
                .password(passwordEncoder.encode("password123"))
                .dateOfBirth(LocalDate.of(1995, 8, 15))
                .gender("Female")
                .anxietyLevel(6.5)
                .heartRate(75.0)
                .therapyGoal("Reduce Anxiety")
                .build();

        PatientProfile patient2 = PatientProfile.builder()
                .fullName("Bob Brown")
                .email("bob@example.com")
                .userName("bobbrown")
                .password(passwordEncoder.encode("password123"))
                .dateOfBirth(LocalDate.of(1990, 3, 10))
                .gender("Male")
                .anxietyLevel(5.0)
                .heartRate(80.0)
                .therapyGoal("Improve Sleep Quality")
                .build();

        patientRepo.saveAll(Arrays.asList(patient1, patient2));

        // === 3. Create Scenarios ===
        Scenario scenario1 = Scenario.builder()
                .name("Calm Beach")
                .description("A peaceful beach scenario to induce relaxation.")
                .createdBy(therapist1)
                .build();

        Scenario scenario2 = Scenario.builder()
                .name("Quiet Forest")
                .description("A serene forest experience for calming the mind.")
                .createdBy(therapist2)
                .build();

        scenarioRepo.saveAll(Arrays.asList(scenario1, scenario2));

        // === 4. Create Customizations ===
        Customization customization1 = Customization.builder()
                .therapist(therapist1)
                .scenario(scenario1)
                .changesDescription("Increased ocean wave sounds.")
                .build();

        Customization customization2 = Customization.builder()
                .therapist(therapist2)
                .scenario(scenario2)
                .changesDescription("Added birds chirping sounds.")
                .build();

        customizationRepo.saveAll(Arrays.asList(customization1, customization2));

        // === 5. Create Progress Trackers ===
        ProgressTracker tracker1 = ProgressTracker.builder()
                .patient(patient1)
                .improvementScore(2.5)
                .build();

        ProgressTracker tracker2 = ProgressTracker.builder()
                .patient(patient2)
                .improvementScore(3.0)
                .build();

        progressTrackerRepo.saveAll(Arrays.asList(tracker1, tracker2));

        // === 6. Create Sessions ===
        Session session1 = Session.builder()
                .therapist(therapist1)
                .patient(patient1)
                .sessionDate(LocalDateTime.now().minusDays(5))
                .sessionDuration(60)
                .scenarioUsed(scenario1.getName())
                .feedback("Very calming experience.")
                .progressTracker(tracker1)
                .build();

        Session session2 = Session.builder()
                .therapist(therapist2)
                .patient(patient2)
                .sessionDate(LocalDateTime.now().minusDays(3))
                .sessionDuration(45)
                .scenarioUsed(scenario2.getName())
                .feedback("Felt very relaxed afterwards.")
                .progressTracker(tracker2)
                .build();

        sessionRepo.saveAll(Arrays.asList(session1, session2));

        // === 7. Create Physiological Data ===
        PhysiologicalData data1 = PhysiologicalData.builder()
                .patient(patient1)
                .heartRate(72.0)
                .anxietyScore(5.5)
                .respirationRate(16.0)
                .bloodPressure("120/80")
                .notes("Patient showed mild signs of anxiety.")
                .timestamp(LocalDateTime.now().minusDays(5))
                .build();

        PhysiologicalData data2 = PhysiologicalData.builder()
                .patient(patient2)
                .heartRate(78.0)
                .anxietyScore(4.8)
                .respirationRate(18.0)
                .bloodPressure("118/76")
                .notes("Patient appeared relaxed. No signs of stress.")
                .timestamp(LocalDateTime.now().minusDays(3))
                .build();

        physiologicalDataRepo.saveAll(Arrays.asList(data1, data2));
	
    }
    
    
    
    
    
}
