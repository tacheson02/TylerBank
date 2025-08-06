package tylerbank.web.app.TylerBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tylerbank.web.app.TylerBank.dto.UserDto;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer for user management operations.
 * @Since v1.3
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Logic for creating(registering) a new user.
     * Saves the user in the repository.
     * @param userDto
     * @return The saved user
     * @since v1.3
     */
    public User registerUser(UserDto userDto) {
        User user = mapToUser(userDto);
        return userRepository.save(user);
    }

    /**
     * Login logic for a user.
     * @param userDto
     * @return authObject if successful, otherwise throws UsernameNotFoundException
     * @since v1.3
     */
    public Map<String, Object> authenticateUser(UserDto userDto) {
        //Creates a new map to hold the authentication result
        Map<String, Object> authObject = new HashMap<String, Object>();
        //Loads user details by username
        User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        //If user not found, throw UsernameNotFoundException
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        //Authenticates the user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        //Generates token and stores it in the authObject map
        authObject.put("token", "Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
        //Stores the user in the authObject map
        authObject.put("user", user);
        return authObject;
    }

    /**
     * Converts UserDto to User entity.
     * @param userDto
     * @return User entity
     * @since v1.3
     */
    private User mapToUser(UserDto userDto) {
        return User.builder()
                .lastname(userDto.getLastname())
                .firstname(userDto.getFirstname())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .tag("TylerBank_" + userDto.getUsername())
                .dob(userDto.getDob())
                .roles(List.of("USER"))
                .build();
    }
}
