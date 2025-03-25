package ca.sheridancollege.ngquocth.controllers;

import java.util.List;

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
import ca.sheridancollege.ngquocth.beans.PhysiologicalData;
import ca.sheridancollege.ngquocth.repositories.PatientProfileRepository;
import ca.sheridancollege.ngquocth.repositories.PhysiologicalDataRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PhysiologicalDataController {
	
	private final PhysiologicalDataRepository physioRepo;
    private final PatientProfileRepository patientRepo;
    


//PATIENT - can only VIEW their record    
    @GetMapping("/patients/my-physio-data")
    public ResponseEntity<List<PhysiologicalData>> getMyPhysioData(@AuthenticationPrincipal UserDetails userDetails) {
        
    	String email = userDetails.getUsername();
        PatientProfile patient = patientRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return ResponseEntity.ok(physioRepo.findByPatient(patient));
    }
    
    
//THERAPIST can do CRUD with the record for the PATIENT
    
    //Therapist view data of a specific patient
    @GetMapping("/therapists/patient/{patientId}/physio-data")
    public ResponseEntity<List<PhysiologicalData>> getPatientData(@PathVariable Long patientId) {
        PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(physioRepo.findByPatient(patient));
    }
    
    //Therapist create a new record for a patient
    @PostMapping("/therapists/patient/{patientId}/physio-data")
    public ResponseEntity<PhysiologicalData> createData(@PathVariable Long patientId, @RequestBody PhysiologicalData data) {
        
    	PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        data.setId(null);
        data.setPatient(patient);
        
        return ResponseEntity.ok(physioRepo.save(data));
    }
    
    
    
    //Therapist edit
    @PutMapping("/therapists/physio-data/{dataId}")
    public ResponseEntity<PhysiologicalData> updateData(@PathVariable Long dataId, @RequestBody PhysiologicalData newData) {
        
    	PhysiologicalData existing = physioRepo.findById(dataId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        existing.setHeartRate(newData.getHeartRate());
        existing.setAnxietyScore(newData.getAnxietyScore());
        existing.setRespirationRate(newData.getRespirationRate());
        existing.setBloodPressure(newData.getBloodPressure());
        existing.setNotes(newData.getNotes());
        existing.setTimestamp(newData.getTimestamp());

        return ResponseEntity.ok(physioRepo.save(existing));
    }
    
    
    
    
  //Therapist delete
    @DeleteMapping("/therapists/physio-data/{dataId}")
    public ResponseEntity<?> deleteData(@PathVariable Long dataId) {
        if (!physioRepo.existsById(dataId)) {
            return ResponseEntity.notFound().build();
        }
        physioRepo.deleteById(dataId);
        return ResponseEntity.ok().build();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
