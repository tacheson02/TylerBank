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
import org.springframework.web.client.RestTemplate;
import tylerbank.web.app.TylerBank.repository.UserRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Security configuration for the users authentication and authorization
 * @since v1.1
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    /**
     * Method to look up username
     * @return username lookup
     * @since v1.1
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userRepository::findByUsernameIgnoreCase;
    }

    /**
     * Provides an instance of BCryptPasswordEncoder for password encoding.
     * When users are added/log-in their password is run here to hash it.
     * @return BCryptPasswordEncoder for password encoding.
     * @since v1.1
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a DaoAuthenticationProvider with the ability to lookup user and check password
     * @return
     * @since v1.1
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }

    /**
     * Exposes the AuthenticationManager for use in other parts of your application
     * @param config
     * @return AuthenticationManager
     * @throws Exception
     * @since v1.1
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Exposes a RestTemplate for making HTTP requests
     * @return
     * @since v2.3
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates a ScheduledExecutorService for running scheduled tasks
     * @return
     * @since v2.3
     */
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }
}
