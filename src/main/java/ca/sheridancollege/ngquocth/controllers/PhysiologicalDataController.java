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
import ca.sheridancollege.ngquocth.beans.PhysiologicalData;
import ca.sheridancollege.ngquocth.repositories.PhysiologicalDataRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/physiological-data")
public class PhysiologicalDataController {
	
	private final PhysiologicalDataRepository dataRepo;


    @GetMapping(value = {"", "/"})
    public List<PhysiologicalData> getAllPhysiologicalData() {
        return dataRepo.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<PhysiologicalData> getPhysiologicalDataById(@PathVariable Long id) {
        return dataRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    
    @PostMapping(value={""}, headers= {"Content-type=application/json"})
    public PhysiologicalData addPhysiologicalData(@RequestBody PhysiologicalData data) {
        data.setDataId(null); 
        return dataRepo.save(data);
    }


    
    @PutMapping(value = {"/{id}"}, headers= {"Content-type=application/json"})
    public ResponseEntity<PhysiologicalData> updatePhysiologicalData(@PathVariable Long id, @RequestBody PhysiologicalData data) {
        Optional<PhysiologicalData> existingData = dataRepo.findById(id);
        if (existingData.isPresent()) {
            data.setDataId(id);
            return ResponseEntity.ok(dataRepo.save(data));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhysiologicalData(@PathVariable Long id) {
        if (dataRepo.existsById(id)) {
            dataRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
