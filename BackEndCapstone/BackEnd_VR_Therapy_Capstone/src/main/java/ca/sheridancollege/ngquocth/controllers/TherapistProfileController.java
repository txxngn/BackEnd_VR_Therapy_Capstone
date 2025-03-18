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

import ca.sheridancollege.ngquocth.beans.TherapistProfile;
import ca.sheridancollege.ngquocth.repositories.TherapistProfileRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/therapists")
public class TherapistProfileController {

	private final TherapistProfileRepository therapistRepo;

    //get all therapists
    @GetMapping(value = {"", "/"})
    public List<TherapistProfile> getAllTherapists() {
        return therapistRepo.findAll();
    }

    //get therapist by ID
    @GetMapping("/{id}")
    public ResponseEntity<TherapistProfile> getTherapistById(@PathVariable Long id) {
        return therapistRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //create new therapist
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public TherapistProfile addTherapist(@RequestBody TherapistProfile therapist) {
        therapist.setUserId(null);  //ensure ID is generated from User 
        return therapistRepo.save(therapist);
    }

    //Update existing therapist
    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<TherapistProfile> updateTherapist(@PathVariable Long id, @RequestBody TherapistProfile therapist) {
        Optional<TherapistProfile> existingTherapist = therapistRepo.findById(id);
        if (existingTherapist.isPresent()) {
            therapist.setUserId(id);
            return ResponseEntity.ok(therapistRepo.save(therapist));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTherapist(@PathVariable Long id) {
        if (therapistRepo.existsById(id)) {
            therapistRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    
    
    
}
