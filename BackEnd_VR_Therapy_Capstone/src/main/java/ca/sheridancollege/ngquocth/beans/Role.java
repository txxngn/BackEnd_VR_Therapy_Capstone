package ca.sheridancollege.ngquocth.beans;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ADMIN,
    THERAPIST,
    PATIENT;

	@Override
    public String getAuthority() {
        return name();
    }

}
