package ca.sheridancollege.ngquocth.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.sheridancollege.ngquocth.beans.ProgressTracker;
import ca.sheridancollege.ngquocth.repositories.ProgressTrackerRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/progress-trackers")
public class ProgressTrackerController {

	private final ProgressTrackerRepository progressTrackerRepo;


    @GetMapping(value = {"", "/"})
    public List<ProgressTracker> getAllProgressTrackers() {
        return progressTrackerRepo.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProgressTracker> getProgressTrackerById(@PathVariable Long id) {
        return progressTrackerRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public ProgressTracker addProgressTracker(@RequestBody ProgressTracker tracker) {
        tracker.setTrackerId(null); 
        return progressTrackerRepo.save(tracker);
    }


    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<ProgressTracker> updateProgressTracker(@PathVariable Long id, @RequestBody ProgressTracker tracker) {
        Optional<ProgressTracker> existingTracker = progressTrackerRepo.findById(id);
        if (existingTracker.isPresent()) {
            tracker.setTrackerId(id);
            return ResponseEntity.ok(progressTrackerRepo.save(tracker));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgressTracker(@PathVariable Long id) {
        if (progressTrackerRepo.existsById(id)) {
            progressTrackerRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    
}
