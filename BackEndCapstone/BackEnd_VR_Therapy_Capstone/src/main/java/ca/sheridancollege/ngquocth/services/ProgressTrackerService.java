package ca.sheridancollege.ngquocth.services;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import ca.sheridancollege.ngquocth.beans.PatientProfile;
import ca.sheridancollege.ngquocth.beans.PhysiologicalData;
import ca.sheridancollege.ngquocth.beans.ProgressTracker;
import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.repositories.PatientProfileRepository;
import ca.sheridancollege.ngquocth.repositories.PhysiologicalDataRepository;
import ca.sheridancollege.ngquocth.repositories.ProgressTrackerRepository;
import ca.sheridancollege.ngquocth.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressTrackerService {

	private final PatientProfileRepository patientRepo;
    private final SessionRepository sessionRepo;
    private final PhysiologicalDataRepository physioRepo;
    private final ProgressTrackerRepository trackerRepo;

    
    /*
     * This method calculates a 10-point improvement score for a patient
     * based on both physiological changes and engagement behavior.
     * 
     * Scoring system:
     * - Anxiety Score Change (3 pts)
     * - Heart Rate Change (2 pts)
     * - Number of Sessions (2 pts)
     * - Average Session Duration (1 pt)
     * - Feedback Quality (2 pts)
     */
    
 
    public double calculateImprovementScore(PatientProfile patient) {

        // Get physiological data and sort chronologically
        List<PhysiologicalData> physioList = physioRepo.findByPatient(patient);
        physioList.sort(Comparator.comparing(PhysiologicalData::getTimestamp));

        double anxietyPoints = 0.0;
        double heartRatePoints = 0.0;

        if (physioList.size() >= 2) {
            double firstAnxiety = physioList.get(0).getAnxietyScore();
            double lastAnxiety = physioList.get(physioList.size() - 1).getAnxietyScore();
            double anxietyDrop = firstAnxiety - lastAnxiety;

            if (anxietyDrop >= 2.0) anxietyPoints = 3.0;
            else if (anxietyDrop >= 1.0) anxietyPoints = 2.0;
            else if (anxietyDrop >= 0.5) anxietyPoints = 1.0;

            double firstHR = physioList.get(0).getHeartRate();
            double lastHR = physioList.get(physioList.size() - 1).getHeartRate();
            double hrDrop = firstHR - lastHR;

            if (hrDrop >= 10.0) heartRatePoints = 2.0;
            else if (hrDrop >= 5.0) heartRatePoints = 1.0;
        }

        // Get session data
        List<Session> sessions = sessionRepo.findByPatient(patient);
        int sessionCount = sessions.size();
        double sessionPoints = 0.0;
        double durationPoints = 0.0;
        double feedbackPoints = 0.0;

        if (sessionCount >= 6) sessionPoints = 2.0;
        else if (sessionCount >= 3) sessionPoints = 1.0;

        // Calculate average session duration
        double avgDuration = sessions.stream()
            .filter(s -> s.getSessionDuration() != null)
            .mapToInt(Session::getSessionDuration)
            .average()
            .orElse(0.0);

        if (avgDuration >= 45) durationPoints = 1.0;

        // Calculate average feedback length
        double avgFeedbackLength = sessions.stream()
            .mapToInt(s -> s.getFeedback() != null ? s.getFeedback().trim().length() : 0)
            .average()
            .orElse(0.0);

        if (avgFeedbackLength >= 100) feedbackPoints = 2.0;
        else if (avgFeedbackLength >= 50) feedbackPoints = 1.0;

        // Final score
        double totalScore = anxietyPoints + heartRatePoints + sessionPoints + durationPoints + feedbackPoints;

        // Ensure max is 10
        return Math.min(10.0, totalScore);
    }
    
    
    
    
    
    
    

    /*
     * Recalculates and updates the improvement score for a specific patient.
     * @return The updated ProgressTracker object
     */
    public ProgressTracker updateTrackerScore(String email) {
        PatientProfile patient = patientRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        ProgressTracker tracker = trackerRepo.findByPatient_Email(email)
            .orElseThrow(() -> new RuntimeException("Progress tracker not found"));

        double newScore = calculateImprovementScore(patient);
        tracker.setImprovementScore(newScore);
        return trackerRepo.save(tracker);
    }

    
    
    
    
    
    
    public ProgressTracker getOwnTracker(String patientEmail) {
        return trackerRepo.findByPatient_Email(patientEmail)
            .orElseThrow(() -> new RuntimeException("Progress tracker not found for the current user."));
    }

    
    
    
    
	
}
