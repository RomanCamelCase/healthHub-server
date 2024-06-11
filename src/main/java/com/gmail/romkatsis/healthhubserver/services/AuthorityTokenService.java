package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.ChangePasswordRequest;
import com.gmail.romkatsis.healthhubserver.enums.Authority;
import com.gmail.romkatsis.healthhubserver.enums.Gender;
import com.gmail.romkatsis.healthhubserver.exceptions.InvalidCodeException;
import com.gmail.romkatsis.healthhubserver.models.AuthorityToken;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.AuthorityTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthorityTokenService {

    private final AuthorityTokenRepository authorityTokenRepository;

    private final UserService userService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final int refreshPasswordTokenDuration;

    @Autowired
    public AuthorityTokenService(AuthorityTokenRepository authorityTokenRepository,
                                 UserService userService,
                                 @Value("${authorities.tokens.refresh-password-token.duration}") int refreshPasswordTokenDuration, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.authorityTokenRepository = authorityTokenRepository;
        this.userService = userService;
        this.refreshPasswordTokenDuration = refreshPasswordTokenDuration;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendResetPasswordEmail(String email) {
        User user = userService.findUserByEmail(email);
        AuthorityToken authorityToken = new AuthorityToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plus(refreshPasswordTokenDuration, ChronoUnit.MILLIS),
                user,
                Authority.RESET_PASSWORD
        );
        authorityTokenRepository.save(authorityToken);
        emailService.sendEmail(user.getEmail(), "Password Reset Code",
                generatePasswordResetBodyText(user, authorityToken));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        AuthorityToken token = authorityTokenRepository
                .findByTokenAndAuthority(changePasswordRequest.getToken(), Authority.RESET_PASSWORD)
                .orElseThrow(() -> new InvalidCodeException("Invalid password refresh token"));
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidCodeException("Too old password refresh token. Try again");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    }

    private String generatePasswordResetBodyText(User user, AuthorityToken authorityToken) {
        StringBuilder passwordResetBodyText = new StringBuilder();
        passwordResetBodyText.append(user.getGender() == Gender.MALE ? "Шановний" : "Шановна");
        passwordResetBodyText.append("%s %s".formatted(user.getFirstName(), user.getLastName()));
        passwordResetBodyText.append("Ви надіслали запит на скидання пароля. " +
                "Будь ласка, використовуйте код нижче, щоб змінити свій пароль:\n%s"
                        .formatted(authorityToken.getToken()));
        return passwordResetBodyText.toString();
    }
}
