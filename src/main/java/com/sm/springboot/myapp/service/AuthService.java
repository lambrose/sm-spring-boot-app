package com.sm.springboot.myapp.service;

import com.sm.springboot.myapp.dto.RegisterRequest;
import com.sm.springboot.myapp.exception.SpringAppException;
import com.sm.springboot.myapp.dto.NotificationEmail;
import com.sm.springboot.myapp.model.User;
import com.sm.springboot.myapp.model.VerificationToken;
import com.sm.springboot.myapp.repository.UserRepository;
import com.sm.springboot.myapp.repository.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final MailService mailService;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(), "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringAppException("Invalid Token")));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringAppException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
