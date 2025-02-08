package dev.rvincent.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final AtlassianOAuth2UserService atlassianOAuth2UserService;

    public SecurityConfig(AtlassianOAuth2UserService atlassianOAuth2UserService) {
        this.atlassianOAuth2UserService = atlassianOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(atlassianOAuth2UserService)))
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }
}
