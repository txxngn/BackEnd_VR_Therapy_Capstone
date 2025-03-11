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

import ca.sheridancollege.ngquocth.beans.Scenario;
import ca.sheridancollege.ngquocth.repositories.ScenarioRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/scenarios")
public class ScenarioController {
	
	private final ScenarioRepository scenarioRepo;

    //get all scenarios
    @GetMapping(value = {"", "/"})
    public List<Scenario> getAllScenarios() {
        return scenarioRepo.findAll();
    }

    //get scenario by ID
    @GetMapping("/{id}")
    public ResponseEntity<Scenario> getScenarioById(@PathVariable Long id) {
        return scenarioRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //create new scenario
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public Scenario addScenario(@RequestBody Scenario scenario) {
        scenario.setScenarioId(null);  
        return scenarioRepo.save(scenario);
    }

    //update
    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<Scenario> updateScenario(@PathVariable Long id, @RequestBody Scenario scenario) {
        Optional<Scenario> existingScenario = scenarioRepo.findById(id);
        if (existingScenario.isPresent()) {
            scenario.setScenarioId(id);
            return ResponseEntity.ok(scenarioRepo.save(scenario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        if (scenarioRepo.existsById(id)) {
            scenarioRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
    

}
