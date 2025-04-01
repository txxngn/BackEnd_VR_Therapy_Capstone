package ca.sheridancollege.ngquocth.controllers;


import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import ca.sheridancollege.ngquocth.beans.ProgressTracker;
import ca.sheridancollege.ngquocth.models.EmailRequestDTO;
import ca.sheridancollege.ngquocth.repositories.ProgressTrackerRepository;
import ca.sheridancollege.ngquocth.services.ProgressTrackerService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProgressTrackerController {


	private final ProgressTrackerRepository progressTrackerRepo;
    private final ProgressTrackerService trackerService;
    
    
//FOR PATIENT
    
    //Patient can only view their progress
    @GetMapping("/patients/my-progress")
    public ResponseEntity<?> getOwnProgress(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            ProgressTracker tracker = trackerService.getOwnTracker(userDetails.getUsername());
            return ResponseEntity.ok(tracker);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
//FOR THERAPIST
    
    //Therapist views all progress of all patient he in charge of
    @GetMapping("/therapists/progress")
    public List<ProgressTracker> getAllTrackers() {
        return progressTrackerRepo.findAll();
    }
    
    
    
    
    //Therapist views progress of a specific patient by id
    @GetMapping("/therapists/progress/{id}")
    public ResponseEntity<ProgressTracker> getTrackerById(@PathVariable Long id) {
        return progressTrackerRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    

 
    
    
    //Therapist force recalculates a patient's progress score.
    @PostMapping("/therapists/progress/recalculate")
    public ResponseEntity<?> recalculateScore(@RequestBody EmailRequestDTO request) {
        try {
            ProgressTracker updated = trackerService.updateTrackerScore(request.getEmail());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
