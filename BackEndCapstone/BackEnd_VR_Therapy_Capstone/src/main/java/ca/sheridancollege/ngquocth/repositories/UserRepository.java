package ca.sheridancollege.ngquocth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ngquocth.beans.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
	//find a user by email for authentication
	public Optional<User> findByEmail(String email);
}
