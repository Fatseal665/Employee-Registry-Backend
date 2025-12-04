package se.sti.employee_registry.view;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.sti.employee_registry.user.CustomUser;
import se.sti.employee_registry.user.CustomUserRepository;
import se.sti.employee_registry.user.authority.UserRole;
import se.sti.employee_registry.user.dto.CustomUserCreationDTO;
import se.sti.employee_registry.user.dto.CustomUserLoginDTO;
import se.sti.employee_registry.user.dto.CustomUserResponseDTO;
import se.sti.employee_registry.user.mapper.CustomUserMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class CustomViewController {

    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomViewController(
            CustomUserRepository customUserRepository,
            CustomUserMapper customUserMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid CustomUserLoginDTO dto) {
    }

    @PostMapping("/logout")
    public void logout() {
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "Admin access granted";
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> adminDelete(@PathVariable UUID id) {
        if (!customUserRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customUserRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @Valid @RequestBody CustomUserCreationDTO dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        CustomUser user = customUserMapper.toEntity(dto);
        user.setPassword(user.getPassword(), passwordEncoder);
        user.setRoles(Set.of(UserRole.EMPLOYEE));
        customUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-all")
    public List<CustomUserResponseDTO> getAll() {
        return customUserRepository.findAll()
                .stream()
                .map(customUserMapper::toResponseDTO)
                .toList();
    }
}
