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
import ca.sheridancollege.ngquocth.services.ProgressTrackerService;
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
    private final ProgressTrackerService trackerService;

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
        customizationRepo.saveAll(Arrays.asList(
                Customization.builder().therapist(therapist1).scenario(scenario1).changesDescription("Increased ocean wave sounds.").build(),
                Customization.builder().therapist(therapist2).scenario(scenario2).changesDescription("Added birds chirping sounds.").build()
            ));


        
        // === 5. Create Progress Trackers ===
        ProgressTracker tracker1 = ProgressTracker.builder().patient(patient1).improvementScore(0.0).build();
        progressTrackerRepo.save(tracker1);
        patient1.setProgressTracker(tracker1);

        ProgressTracker tracker2 = ProgressTracker.builder().patient(patient2).improvementScore(0.0).build();
        progressTrackerRepo.save(tracker2);
        patient2.setProgressTracker(tracker2);
        patientRepo.saveAll(Arrays.asList(patient1, patient2));
        
              
        
        
        // === 6. Create Sessions ===
        sessionRepo.saveAll(Arrays.asList(
        		Session.builder().therapist(therapist1).patient(patient1).sessionDate(LocalDateTime.now().minusDays(10)).sessionDuration(60).scenarioUsed("Calm Beach").feedback("It was helpful. I practiced deep breathing.").progressTracker(tracker1).build(),
                Session.builder().therapist(therapist1).patient(patient1).sessionDate(LocalDateTime.now().minusDays(5)).sessionDuration(50).scenarioUsed("Calm Beach").feedback("Today I felt a big shift. My anxiety reduced drastically. I’m more confident. I practiced visualization.").progressTracker(tracker1).build(),
                Session.builder().therapist(therapist1).patient(patient1).sessionDate(LocalDateTime.now().minusDays(1)).sessionDuration(55).scenarioUsed("Calm Beach").feedback("One of the most effective sessions so far. Long guided breathing with detailed exposure techniques.").progressTracker(tracker1).build(),

                Session.builder().therapist(therapist2).patient(patient2).sessionDate(LocalDateTime.now().minusDays(3)).sessionDuration(45).scenarioUsed("Quiet Forest").feedback("Very relaxing. I felt peaceful and my anxiety reduced.").progressTracker(tracker2).build(),
                Session.builder().therapist(therapist2).patient(patient2).sessionDate(LocalDateTime.now().minusDays(1)).sessionDuration(50).scenarioUsed("Quiet Forest").feedback("It helped a little. I was slightly more calm after the session.").progressTracker(tracker2).build(),
                Session.builder().therapist(therapist2).patient(patient2).sessionDate(LocalDateTime.now()).sessionDuration(48).scenarioUsed("Quiet Forest").feedback("This session was decent. Not as strong an impact, but still valuable.").progressTracker(tracker2).build()
            ));
        
        

        // === 7. Create Physiological Data ===
        physiologicalDataRepo.saveAll(Arrays.asList(
                PhysiologicalData.builder().patient(patient1).heartRate(75.0).anxietyScore(6.5).respirationRate(18.0).bloodPressure("130/85").notes("Initial assessment").timestamp(LocalDateTime.now().minusDays(10)).build(),
                PhysiologicalData.builder().patient(patient1).heartRate(62.0).anxietyScore(3.0).respirationRate(14.0).bloodPressure("115/75").notes("Mid treatment").timestamp(LocalDateTime.now().minusDays(5)).build(),
                PhysiologicalData.builder().patient(patient1).heartRate(60.0).anxietyScore(3.0).respirationRate(13.0).bloodPressure("110/70").notes("Latest improvement").timestamp(LocalDateTime.now()).build(),

                PhysiologicalData.builder().patient(patient2).heartRate(80.0).anxietyScore(5.0).respirationRate(18.0).bloodPressure("125/82").notes("Baseline reading").timestamp(LocalDateTime.now().minusDays(5)).build(),
                PhysiologicalData.builder().patient(patient2).heartRate(75.0).anxietyScore(4.5).respirationRate(16.0).bloodPressure("120/78").notes("Slight progress").timestamp(LocalDateTime.now()).build()
            ));
	
        
        
        

        
        
        //Trigger score calculation
        trackerService.updateTrackerScore(patient1.getEmail());
        trackerService.updateTrackerScore(patient2.getEmail());
        
        
        
        
        
        
        
        
        
    }
    
 
}
