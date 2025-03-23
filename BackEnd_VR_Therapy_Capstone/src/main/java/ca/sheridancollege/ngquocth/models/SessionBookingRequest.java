package ca.sheridancollege.ngquocth.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SessionBookingRequest {

	private Long patientId;
    private String scenarioUsed;
    private LocalDateTime sessionDate;
    private Integer sessionDuration;
    private String feedback;
    
}
