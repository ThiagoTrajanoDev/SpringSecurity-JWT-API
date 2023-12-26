package com.SecurityAplication.AutenticationApp.repository;

import com.SecurityAplication.AutenticationApp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository  extends JpaRepository<User, Long> {
        UserDetails findByLogin(String login);
}
