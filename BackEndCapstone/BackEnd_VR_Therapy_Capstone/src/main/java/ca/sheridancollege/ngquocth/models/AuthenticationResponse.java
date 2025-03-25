package ca.sheridancollege.ngquocth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	//used to send back the JWT token after successful authentication.
	private String token;
}
