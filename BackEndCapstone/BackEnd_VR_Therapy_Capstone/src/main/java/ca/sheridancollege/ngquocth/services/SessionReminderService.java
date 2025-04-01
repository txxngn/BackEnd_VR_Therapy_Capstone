package ca.sheridancollege.ngquocth.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ca.sheridancollege.ngquocth.beans.Session;
import ca.sheridancollege.ngquocth.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "email.enabled", havingValue = "true", matchIfMissing = false) //temporary
public class SessionReminderService {

	private final SessionRepository sessionRepo;
    private final EmailService emailService;
    
    
    
    

    //runs every hour to check for sessions in the next hour
    @Scheduled(fixedRate = 60 * 60 * 1000) // runs every hour
    public void sendSessionReminders() {
        LocalDateTime now = LocalDateTime.now();

        // 1-Hour Reminder
        LocalDateTime inOneHour = now.plusHours(1);
        List<Session> oneHourSessions = sessionRepo.findBySessionDateBetween(now, inOneHour);
        for (Session session : oneHourSessions) {
            if (!session.isReminderSent1Hour()) {
                sendReminderEmail(session, "1-hour");
                session.setReminderSent1Hour(true);
                sessionRepo.save(session);
            }
        }

        // 24-Hour Reminder
        LocalDateTime start24 = now.plusHours(23);
        LocalDateTime end24 = now.plusHours(25); // allows 1 hour window buffer
        List<Session> twentyFourHourSessions = sessionRepo.findBySessionDateBetween(start24, end24);
        for (Session session : twentyFourHourSessions) {
            if (!session.isReminderSent24Hour()) {
                sendReminderEmail(session, "24-hour");
                session.setReminderSent24Hour(true);
                sessionRepo.save(session);
            }
        }
    }

    
    
    
    private void sendReminderEmail(Session session, String type) {
        String patientEmail = session.getPatient().getEmail();
        String therapistEmail = session.getTherapist().getEmail();

        String subject = "Therapy Session Reminder (" + type + " notice)";
        String body = String.format(
            "Hello %s,\n\nThis is a %s reminder that you have a therapy session scheduled at %s.\n\nTherapist: %s\nScenario: %s\n\n- VR Therapy Team",
            session.getPatientName(),
            type,
            session.getSessionDate().toString(),
            session.getTherapistName(),
            session.getScenarioUsed()
        );

        emailService.sendReminder(patientEmail, subject, body);

        
        
        //Therapist version
        String therapistBody = String.format(
            "Reminder: You have a therapy session with %s in %s.\nTime: %s\nScenario: %s",
            session.getPatientName(),
            type,
            session.getSessionDate().toString(),
            session.getScenarioUsed()
        );

        emailService.sendReminder(therapistEmail, subject, therapistBody);
    }
    
    
    
    
    
    
    
    
}
