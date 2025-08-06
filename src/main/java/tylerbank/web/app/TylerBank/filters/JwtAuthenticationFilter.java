package tylerbank.web.app.TylerBank.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.service.JwtService;

import java.io.IOException;

// This class validates each request for a valid JWT token and sets the security context with the user's details.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //Checks for the authorization header at the beginning of the request
        String jwtToken = request.getHeader("Authorization");

        //If there is no header or if token is expired, pass request to next filter and stop processing current filter
        if(jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))) {
            filterChain.doFilter(request, response);
            return;
        }

        //Remove bearer prefix
        jwtToken = jwtToken.startsWith("Bearer ") ? jwtToken.substring(7) : jwtToken;
        //Extract the subject (username) from the token
        String subject = jwtService.extractSubject(jwtToken);
        //Find user in the database by username and set the security context with the users details
        User user = (User) userDetailsService.loadUserByUsername(subject);
        //Get the security context
        var context = SecurityContextHolder.getContext();

        //If user is found and not authenticated, authenticate the user
        if(user != null && context.getAuthentication() == null){
            var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(request);
            context.setAuthentication(authenticationToken);
        }

        //Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
