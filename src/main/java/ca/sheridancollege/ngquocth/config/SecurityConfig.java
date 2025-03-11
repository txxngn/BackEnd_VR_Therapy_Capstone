package ca.sheridancollege.ngquocth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Allow access to H2 console without authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            // Disable CSRF for H2 Console access
            .csrf(csrf -> csrf.disable())
            // Allow frames for H2 console
            .headers(headers -> headers.frameOptions().sameOrigin())
            // Form-based login for other endpoints
            .formLogin();

        return http.build();
    }
	
	
}
