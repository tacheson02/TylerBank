package tylerbank.web.app.TylerBank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tylerbank.web.app.TylerBank.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authFilter;
    private final AuthenticationProvider authenticationProvider;

    //Defines the security filter chain for the application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //Enables Cross-Origin Resource Sharing (CORS) *Defined in method below*
        http.cors((c) -> c.configurationSource(corsConfigurationSource()))
            //Disables Cross-Site Request Forgery (CSRF) *Stateless API with JWTs allows feature to be disabled safely*
            .csrf(AbstractHttpConfigurer::disable)
                //Defines which endpoints are available to public and which need authentication
                .authorizeHttpRequests(
                        /* Public endpoints: /auth (Login) and /register (Register)
                         * Private endpoints: everything else
                         */
                        request ->
                        request.requestMatchers("/users/auth", "/users/register")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
                )
            //Configures user authentication and authorization
            .authenticationProvider(authenticationProvider)
            //Configures custom JWT filter
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            //Disables session management (stateful)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    //Defines rules for CORS used in above method
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        //Allows API calls from localhost:3000
        corsConfig.addAllowedOrigin("http://localhost:3000");
        //Allows GET, POST, PUT, DELETE requests
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //Applies all CORS configurations to all requests
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}
