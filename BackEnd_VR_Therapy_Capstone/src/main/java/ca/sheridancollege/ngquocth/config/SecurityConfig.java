package ca.sheridancollege.ngquocth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	/*
	 * This class will define:
		Which endpoints are public (like /api/auth/** for login and registration).
		Which endpoints require authentication.
		How JWT authentication will be handled.
	 */
	
	
	private JwtAuthenticationFilter jwtAuthenticationFilter;
    private AuthenticationProvider authenticationProvider;
	
	
	
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll() //public endpoint for auth
                .requestMatchers("/h2-console/**").permitAll() //allow access to H2 console
                .requestMatchers("/api/patients/**").hasRole("PATIENT")     //Restrict access based on role
                .requestMatchers("/api/therapists/**").hasRole("THERAPIST")
                .anyRequest().authenticated() //secure all other endpoints
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions().sameOrigin()) //for H2 Console
            .build();
    }
	
	
}
