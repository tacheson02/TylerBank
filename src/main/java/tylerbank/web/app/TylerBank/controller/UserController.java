package tylerbank.web.app.TylerBank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tylerbank.web.app.TylerBank.dto.UserDto;
import tylerbank.web.app.TylerBank.entity.User;
import tylerbank.web.app.TylerBank.service.UserService;

/**
 * Control layer that exposes endpoints for user management.
 * @since v1.3
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint for registering a new user.
     * @param userDto
     * @return response with the registered user.
     * @since v1.3
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    /**
     * Endpoint for authenticating a user.
     * @param userDto
     * @return response with the authenticated user and authentication token.
     * @since v1.3
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto){
        var authObject = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, (String) authObject.get("token"))
            .body(authObject.get("user"));
    }
}