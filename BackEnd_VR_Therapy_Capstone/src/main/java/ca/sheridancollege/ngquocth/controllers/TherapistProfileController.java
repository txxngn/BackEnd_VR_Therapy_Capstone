package ca.sheridancollege.ngquocth.controllers;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    //get all therapists - not necessary anymore
    @GetMapping(value = {"", "/"})
    public List<TherapistProfile> getAllTherapists() {
        return therapistRepo.findAll();
    }


    //create new therapist - used only by admin now
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public TherapistProfile addTherapist(@RequestBody TherapistProfile therapist) {
        therapist.setUserId(null);  //ensure ID is generated from User 
        return therapistRepo.save(therapist);
    }

    
    
    //view their own profile without needing the ID
    @GetMapping("/profile")
    public ResponseEntity<?> getOwnProfile(@AuthenticationPrincipal UserDetails userDetails) {
        TherapistProfile therapist = therapistRepo.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Therapist not found"));
        return ResponseEntity.ok(therapist);
    }
    
    
    //Secure update profile without expose the id of the therapist
    @PutMapping("/profile")
    public ResponseEntity<?> updateTherapistProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TherapistProfile updatedProfile) {

        TherapistProfile therapist = therapistRepo.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Therapist not found"));

        therapist.setFullName(updatedProfile.getFullName());
        therapist.setDateOfBirth(updatedProfile.getDateOfBirth());
        therapist.setGender(updatedProfile.getGender());
        therapist.setSpecialization(updatedProfile.getSpecialization());
        therapist.setExperienceYears(updatedProfile.getExperienceYears());
        therapist.setLicenseNumber(updatedProfile.getLicenseNumber());

        therapistRepo.save(therapist);
        return ResponseEntity.ok("Therapist profile updated successfully.");
    }

    //Secure delete
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteTherapistProfile(@AuthenticationPrincipal UserDetails userDetails) {
        TherapistProfile therapist = therapistRepo.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Therapist not found"));

        therapistRepo.delete(therapist);
        return ResponseEntity.ok("Therapist profile deleted successfully.");
    }

    
    
    
    
    
}
