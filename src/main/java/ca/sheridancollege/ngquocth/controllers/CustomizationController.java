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

import ca.sheridancollege.ngquocth.beans.Customization;
import ca.sheridancollege.ngquocth.repositories.CustomizationRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/customizations")
public class CustomizationController {

	private final CustomizationRepository customizationRepo;

    
    @GetMapping(value = {"", "/"})
    public List<Customization> getAllCustomizations() {
        return customizationRepo.findAll();
    }

    //Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customization> getCustomizationById(@PathVariable Long id) {
        return customizationRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    //create
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public Customization addCustomization(@RequestBody Customization customization) {
        customization.setCustomizationId(null); 
        return customizationRepo.save(customization);
    }

    //ipdate
    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<Customization> updateCustomization(@PathVariable Long id, @RequestBody Customization customization) {
        Optional<Customization> existingCustomization = customizationRepo.findById(id);
        if (existingCustomization.isPresent()) {
            customization.setCustomizationId(id);
            return ResponseEntity.ok(customizationRepo.save(customization));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomization(@PathVariable Long id) {
        if (customizationRepo.existsById(id)) {
            customizationRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    
}
