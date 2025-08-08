package tylerbank.web.app.TylerBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for User
 * Allows API to use User data without affecting the underlying User model
 * @since 1.2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private Date dob;
    private long tel;
    private String gender;
}
