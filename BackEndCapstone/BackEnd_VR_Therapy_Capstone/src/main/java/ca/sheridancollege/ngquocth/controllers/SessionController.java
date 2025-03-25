package ca.sheridancollege.ngquocth.controllers;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.ngquocth.beans.PatientProfile;
import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.beans.TherapistProfile;
import ca.sheridancollege.ngquocth.models.SessionBookingRequest;
import ca.sheridancollege.ngquocth.repositories.PatientProfileRepository;
import ca.sheridancollege.ngquocth.repositories.SessionRepository;
import ca.sheridancollege.ngquocth.repositories.TherapistProfileRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SessionController {

	
	private final SessionRepository sessionRepo;
	private final PatientProfileRepository patientRepo;
    private final TherapistProfileRepository therapistRepo;

    
//For Patient    
    //patient BOOK/CREATE a therapy session
    @PostMapping("/patients/book-session")
    public ResponseEntity<?> bookSessionAsPatient(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Session session) {

        String email = userDetails.getUsername();
        PatientProfile patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

     //automatically assign a therapist for the session, a session have to have 1 therapist since i set this attribute in Session.java is not null
        TherapistProfile therapist = therapistRepo.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No therapist available"));
        
        
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setSessionId(null);
        
        Session savedSession = sessionRepo.save(session);
        return ResponseEntity.ok(savedSession);
    }

    //patient VIEW their therapy bookings
    @GetMapping("/patients/my-sessions")
    public ResponseEntity<List<Session>> viewPatientSessions(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        PatientProfile patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Session> sessions = sessionRepo.findByPatient(patient);
        return ResponseEntity.ok(sessions);
    }
    
    //patient EDIT their therapy session
    @PutMapping("/patients/edit-session/{sessionId}")
    public ResponseEntity<?> editSessionAsPatient(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long sessionId,
            @RequestBody Session updatedSession) {

        String email = userDetails.getUsername();
        PatientProfile patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Session existingSession = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        //check the patient owns the session
        if (!existingSession.getPatient().getUserId().equals(patient.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only edit your own sessions.");
        }

        //update
        existingSession.setSessionDate(updatedSession.getSessionDate());
        existingSession.setSessionDuration(updatedSession.getSessionDuration());
        existingSession.setScenarioUsed(updatedSession.getScenarioUsed());
        existingSession.setFeedback(updatedSession.getFeedback());

        sessionRepo.save(existingSession);
        return ResponseEntity.ok(existingSession);
    }
    
    
    //Patient can cancel/delete their session
    @DeleteMapping("/patients/cancel-session/{sessionId}")
    public ResponseEntity<String> cancelSessionAsPatient(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails userDetails) {
        
    	String email = userDetails.getUsername();
    	
        PatientProfile patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        //check patient owns the session
        if (!session.getPatient().equals(patient)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only cancel your own sessions.");
        }

        sessionRepo.delete(session);
        return ResponseEntity.ok("Session canceled successfully.");
    }
    
    

//For Therapist
    

    //Therapist Book/Create a therapy session for a Patient
    @PostMapping("/therapists/book-session")
    public ResponseEntity<?> bookSessionAsTherapist(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SessionBookingRequest request) {

    	//authenticate Therapist (from JWT token)
        String email = userDetails.getUsername();
        TherapistProfile therapist = therapistRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        //validate Patient
        PatientProfile patient = patientRepo.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        //create and save Session
        Session session = Session.builder()
                .therapist(therapist)
                .patient(patient)
                .sessionDate(request.getSessionDate())
                .sessionDuration(request.getSessionDuration())
                .scenarioUsed(request.getScenarioUsed())
                .feedback(request.getFeedback())
                .build();

        sessionRepo.save(session);
        
        return ResponseEntity.ok(session);
    }

    //Therapist view their booked sessions
    @GetMapping("/therapists/my-sessions")
    public ResponseEntity<List<Session>> viewTherapistSessions(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        TherapistProfile therapist = therapistRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        List<Session> sessions = sessionRepo.findByTherapist(therapist);
        return ResponseEntity.ok(sessions);
    }
    
    
    
    //Therapist edit a session that booked by them
    @PutMapping("/therapists/edit-session/{sessionId}")
    public ResponseEntity<Session> editSessionAsTherapist(
            @PathVariable Long sessionId,
            @RequestBody SessionBookingRequest sessionRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        TherapistProfile therapist = therapistRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        
        if (!session.getTherapist().equals(therapist)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        
        session.setSessionDate(sessionRequest.getSessionDate());
        session.setSessionDuration(sessionRequest.getSessionDuration());
        session.setScenarioUsed(sessionRequest.getScenarioUsed());
        session.setFeedback(sessionRequest.getFeedback());

        sessionRepo.save(session);
        return ResponseEntity.ok(session);
    }
    
    
    //Therapist can cancel only their own session
    @DeleteMapping("/therapists/cancel-session/{sessionId}")
    public ResponseEntity<String> cancelSessionAsTherapist(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        TherapistProfile therapist = therapistRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        
        if (!session.getTherapist().equals(therapist)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only cancel sessions you booked.");
        }

        sessionRepo.delete(session);
        return ResponseEntity.ok("Session canceled successfully.");
    }
    
    
    
    /*
    //get
    @GetMapping(value = {"", "/"})
    public List<Session> getAllSessions() {
        return sessionRepo.findAll();
    }

    //get by id
    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return sessionRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //create
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public Session addSession(@RequestBody Session session) {
        session.setSessionId(null); // Ensure ID is generated
        return sessionRepo.save(session);
    }

    
    //update session
    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        Optional<Session> existingSession = sessionRepo.findById(id);
        if (existingSession.isPresent()) {
            session.setSessionId(id);
            return ResponseEntity.ok(sessionRepo.save(session));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        if (sessionRepo.existsById(id)) {
            sessionRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    */
    
    
    
    
}
