package com.mobiautobackend.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.mobiautobackend.api.rest.controllers.MemberController.MEMBER_AUTH_PATH;
import static com.mobiautobackend.api.rest.controllers.MemberController.MEMBER_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.OpportunityController.OPPORTUNITY_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.VehicleController.VEHICLE_SELF_PATH;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Autowired
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, MEMBER_AUTH_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, MEMBER_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, OPPORTUNITY_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, VEHICLE_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, VEHICLE_SELF_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, "/teste").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
