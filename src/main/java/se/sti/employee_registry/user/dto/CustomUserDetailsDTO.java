package se.sti.employee_registry.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import se.sti.employee_registry.security.rules.EmailRules;
import se.sti.employee_registry.security.rules.PasswordRules;
import se.sti.employee_registry.security.rules.RolesRules;
import se.sti.employee_registry.user.authority.UserRole;

import java.util.Set;
//TODO Serielaisoenon för att göra om till strängar, oauth2 och rs istället för hs
public record CustomUserDetailsDTO(


        @Valid
        EmailRules email,

        @Valid
        PasswordRules password,

        @NotNull boolean isAccountNonExistent,
        @NotNull boolean isAccountNonLocked,
        @NotNull boolean isCredentialsNonExpired,
        @NotNull boolean isEnabled,

        @Valid
        RolesRules roles

) {
}
