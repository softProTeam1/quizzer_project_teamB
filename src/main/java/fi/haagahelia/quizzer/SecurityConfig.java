package fi.haagahelia.quizzer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers(antMatcher("/css/**")).permitAll()
                                                .requestMatchers(antMatcher("/signup")).permitAll()
                                                .requestMatchers(antMatcher("/saveuser")).permitAll()
                                                .requestMatchers(antMatcher("/api/**")).permitAll()
                                                .requestMatchers(antMatcher("/error")).permitAll()
                                                .requestMatchers(antMatcher("/saveuser")).permitAll()
                                                .requestMatchers(antMatcher("/quizzlist")).permitAll()
                                                // Swagger documentation paths
                                                .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                                                .requestMatchers(antMatcher("/configuration/ui")).permitAll()
                                                .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
                                                .requestMatchers(antMatcher("/configuration/security")).permitAll()
                                                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()

                                                .anyRequest().authenticated())
                                .headers((headers) -> headers
                                                .frameOptions(frameoptions -> frameoptions.disable() // for h2 console
                                                ))
                                .formLogin((formlogin) -> formlogin
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/quizzlist", true)
                                                .permitAll())
                                .logout((logout) -> logout
                                                .permitAll());
                http.cors(Customizer.withDefaults());
                http.csrf((csrf) -> csrf.ignoringRequestMatchers(antMatcher("/api/**")));
                return http.build();
        }

}