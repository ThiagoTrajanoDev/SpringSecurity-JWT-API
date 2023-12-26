package com.SecurityAplication.AutenticationApp.controller;

import com.SecurityAplication.AutenticationApp.DTO.AuthDTO;
import com.SecurityAplication.AutenticationApp.DTO.LoginResponseDTO;
import com.SecurityAplication.AutenticationApp.DTO.RegisterDTO;
import com.SecurityAplication.AutenticationApp.domain.user.User;
import com.SecurityAplication.AutenticationApp.repository.UserRepository;
import com.SecurityAplication.AutenticationApp.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDTO authDTO){
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        Authentication auth = authenticationManager.authenticate(userNamePassword);
        String token =  tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        if(this.userRepository.findByLogin(registerDTO.login()) != null)  {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        User user =  new User(registerDTO.login(),encryptedPassword, registerDTO.role());
        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}

