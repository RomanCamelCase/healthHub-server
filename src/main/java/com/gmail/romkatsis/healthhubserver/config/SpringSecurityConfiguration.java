package com.gmail.romkatsis.healthhubserver.config;

import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import com.gmail.romkatsis.healthhubserver.security.ChainExceptionHandlerFilter;
import com.gmail.romkatsis.healthhubserver.security.DefaultAuthenticationEntryPoint;
import com.gmail.romkatsis.healthhubserver.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final UserRepository userRepository;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ChainExceptionHandlerFilter chainExceptionHandlerFilter;

    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;


    @Autowired
    public SpringSecurityConfiguration(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter, ChainExceptionHandlerFilter chainExceptionHandlerFilter, DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint) {
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.chainExceptionHandlerFilter = chainExceptionHandlerFilter;
        this.defaultAuthenticationEntryPoint = defaultAuthenticationEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> new User(
                        user.getId().toString(),
                        user.getPassword(),
                        user.getRoles()))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with email %s not found".formatted(email)));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(defaultAuthenticationEntryPoint))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                );
        http.addFilterBefore(chainExceptionHandlerFilter, LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
