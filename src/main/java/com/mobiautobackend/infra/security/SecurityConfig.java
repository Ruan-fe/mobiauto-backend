package com.mobiautobackend.infra.security;

import com.mobiautobackend.domain.enumeration.MemberRole;
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

import static com.mobiautobackend.api.rest.controllers.AuthenticationController.AUTHENTICATION_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.DealershipController.DEALERSHIP_RESOURCE_PATH;
import static com.mobiautobackend.api.rest.controllers.DealershipController.DEALERSHIP_SELF_PATH;
import static com.mobiautobackend.api.rest.controllers.MemberController.*;
import static com.mobiautobackend.api.rest.controllers.OpportunityController.*;
import static com.mobiautobackend.api.rest.controllers.VehicleController.*;

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
                        .requestMatchers(HttpMethod.POST, AUTHENTICATION_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, MEMBER_RESOURCE_PATH).hasRole(MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, MEMBER_SELF_PATH).authenticated()
                        .requestMatchers(HttpMethod.POST, DEALERSHIP_RESOURCE_PATH).hasAnyRole(MemberRole.USER.name(), MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, DEALERSHIP_SELF_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, OPPORTUNITY_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, OPPORTUNITY_SELF_PATH).hasAnyRole(MemberRole.ASSISTANT.name(), MemberRole.MANAGER.name(), MemberRole.OWNER.name(), MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, OPPORTUNITY_RESOURCE_PATH).hasAnyRole(MemberRole.ASSISTANT.name(), MemberRole.MANAGER.name(), MemberRole.OWNER.name(), MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, OPPORTUNITY_PATH).hasAnyRole(MemberRole.ASSISTANT.name(), MemberRole.MANAGER.name(), MemberRole.OWNER.name(), MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, VEHICLE_RESOURCE_PATH).hasAnyRole(MemberRole.OWNER.name(), MemberRole.MANAGER.name(), MemberRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, VEHICLE_RESOURCE_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, VEHICLE_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, VEHICLE_SELF_PATH).permitAll()
                        .anyRequest().hasRole(MemberRole.ADMIN.name()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
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
