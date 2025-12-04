package se.sti.employee_registry.user.mapper;

import org.springframework.stereotype.Component;
import se.sti.employee_registry.security.rules.*;
import se.sti.employee_registry.user.CustomUser;
import se.sti.employee_registry.user.dto.CustomUserCreationDTO;
import se.sti.employee_registry.user.dto.CustomUserDetailsDTO;
import se.sti.employee_registry.user.dto.CustomUserResponseDTO;

@Component
public class CustomUserMapper {

    public CustomUser toEntity(CustomUserCreationDTO customUserCreationDTO) {

        return new CustomUser(
                customUserCreationDTO.firstName().value(),
                customUserCreationDTO.lastName().value(),
                customUserCreationDTO.email().value(),
                customUserCreationDTO.password().value(),
                customUserCreationDTO.isCredentialsNonExpired(),
                customUserCreationDTO.isAccountNonLocked(),
                customUserCreationDTO.isCredentialsNonExpired(),
                customUserCreationDTO.isEnabled(),
                customUserCreationDTO.roles().value()
        );
    }

    public CustomUserResponseDTO toResponseDTO(CustomUser customUser) {
        return new CustomUserResponseDTO(
                customUser.getId(),
                new FirstnameRules(customUser.getFirstName()),
                new LastnameRules(customUser.getLastName()),
                new EmailRules(customUser.getEmail()),
                customUser.isAccountNonExistent(),
                customUser.isAccountNonLocked(),
                customUser.isCredentialsNonExpired(),
                customUser.isEnabled(),
                new RolesRules(customUser.getRoles())
        );
    }
    public CustomUserDetailsDTO toUserDetailsDTO(CustomUser customUser) {
        return new CustomUserDetailsDTO(
                new EmailRules(customUser.getEmail()),
                new PasswordRules(customUser.getPassword()),
                customUser.isAccountNonExistent(),
                customUser.isAccountNonLocked(),
                customUser.isAccountNonLocked(),
                customUser.isEnabled(),
                new RolesRules(customUser.getRoles())
        );
    }

    public CustomUser detailDTOToEntity(CustomUserDetailsDTO customUserDetailsDTO) {
        return new CustomUser(
                "",
                "",
                customUserDetailsDTO.email().value(),
                customUserDetailsDTO.password().value(),
                customUserDetailsDTO.isAccountNonExistent(),
                customUserDetailsDTO.isAccountNonLocked(),
                customUserDetailsDTO.isCredentialsNonExpired(),
                customUserDetailsDTO.isEnabled(),
                customUserDetailsDTO.roles().value()
                );
    }

}
