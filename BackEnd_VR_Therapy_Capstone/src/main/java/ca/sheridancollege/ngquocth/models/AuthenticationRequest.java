package ca.sheridancollege.ngquocth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	//used to capture user credentials when they try to log in
	
	private String email;
    private String password;
    
    
}
