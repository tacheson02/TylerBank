package tylerbank.web.app.TylerBank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tylerbank.web.app.TylerBank.repository.UserRepository;

// Security configuration for the application
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    // Simply a secure way to look up username
    @Bean
    public UserDetailsService userDetailsService() {
        return userRepository::findByUsernameIgnoreCase;
    }

    /* Provides an instance of BCryptPasswordEncoder for password encoding.
     * When users are added/log-in their password is run here to hash it.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Creates a DaoAuthenticationProvider with the ability to lookup user and check password
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }

    // Exposes the AuthenticationManager for use in other parts of your application
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
