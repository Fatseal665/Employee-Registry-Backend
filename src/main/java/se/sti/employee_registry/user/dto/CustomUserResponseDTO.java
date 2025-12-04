package se.sti.employee_registry.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import se.sti.employee_registry.security.rules.*;
import se.sti.employee_registry.user.authority.UserRole;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CustomUserResponseDTO {

        private UUID id;
        @Valid
        private FirstnameRules firstName;

        @Valid
        private LastnameRules lastName;

        @Valid
        private EmailRules email;

        @NotNull
        private boolean isAccountNonExistent;


        @NotNull
        private boolean isAccountNonLocked;


        @NotNull
        private boolean isCredentialsNonExpired;


        @NotNull
        private boolean isEnabled;

        @Valid
        private RolesRules roles;

        // Constructor
        public CustomUserResponseDTO(UUID id,
                                     @Valid FirstnameRules firstName,
                                     @Valid LastnameRules lastName,
                                     @Valid EmailRules email,
                                     boolean isAccountNonExistent,
                                     boolean isAccountNonLocked,
                                     boolean isCredentialsNonExpired,
                                     boolean isEnabled,
                                     @Valid RolesRules roles) {
                this.id =id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.isAccountNonExistent = isAccountNonExistent;
                this.isAccountNonLocked = isAccountNonLocked;
                this.isCredentialsNonExpired = isCredentialsNonExpired;
                this.isEnabled = isEnabled;
                this.roles = roles;
        }

        // ----- Getters som returnerar String -----
        public String getFirstName() {
                return firstName.value();  // Antar att FirstnameRules har getValue()
        }

        public void setFirstName(String firstName) {
                this.firstName = new FirstnameRules(firstName);
        }

        public String getLastName() {
                return lastName.value();
        }

        public void setLastName(String lastName) {
                this.lastName = new LastnameRules(lastName);
        }

        public String getEmail() {
                return email.value();
        }

        public void setEmail(String email) {
                this.email = new EmailRules(email);
        }

        public boolean isAccountNonExistent() {
                return isAccountNonExistent;
        }

        public void setAccountNonExistent(boolean accountNonExistent) {
                isAccountNonExistent = accountNonExistent;
        }

        public boolean isAccountNonLocked() {
                return isAccountNonLocked;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
                isAccountNonLocked = accountNonLocked;
        }

        public boolean isCredentialsNonExpired() {
                return isCredentialsNonExpired;
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
                isCredentialsNonExpired = credentialsNonExpired;
        }

        public boolean isEnabled() {
                return isEnabled;
        }

        public void setEnabled(boolean enabled) {
                isEnabled = enabled;
        }

        public Set<UserRole> getRoles() {
                return roles.value();
        }

        public void setRoles(Set<UserRole> roles) {
                this.roles = new RolesRules(roles);
        }

        public UUID getId() {
                return id;
        }

}
