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

import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.repositories.SessionRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

	
	private final SessionRepository sessionRepo;

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
    
    
}
