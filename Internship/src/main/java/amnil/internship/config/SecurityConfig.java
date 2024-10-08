package amnil.internship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Loading custom SecurityFilterChain...");

        http
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/login", "/user/login", "/css/**", "/js/**", "/images/**").permitAll().anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/logout").permitAll())
                .sessionManagement(session -> session
                        .sessionFixation()
                        .migrateSession()
                        .maximumSessions(1)
                        .expiredUrl("/login?expire=true")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
