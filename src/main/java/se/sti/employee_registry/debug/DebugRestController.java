package se.sti.employee_registry.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sti.employee_registry.publisher.EmailPublisher;
import se.sti.employee_registry.user.CustomUser;
import se.sti.employee_registry.user.CustomUserRepository;
import se.sti.employee_registry.user.authority.UserRole;

import java.util.Set;

@RequestMapping("/debug")
@RestController
public class DebugRestController {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository customUserRepository;
    private final EmailPublisher emailPublisher;

    @Autowired
    public DebugRestController(PasswordEncoder passwordEncoder, CustomUserRepository repository, EmailPublisher emailPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.customUserRepository = repository;
        this.emailPublisher = emailPublisher;
    }

    @GetMapping("/create-debug-user")
    public ResponseEntity<String> createDebugAdmin() {
        try {
            customUserRepository.save(
                    new CustomUser(
                            "Benny",
                            "Bengtsson",
                            "Benny123@gmail.com",
                            passwordEncoder.encode("123"),
                            true,
                            true,
                            true,
                            true,
                            Set.of(UserRole.ADMIN)
                    )
            );
            String username = "Benny";
            emailPublisher.sendUserCreated("Benny123@gmail.com", "Benny");

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully ");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists " + e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong " + e.getLocalizedMessage());
        } finally {
            System.out.println("Creating debug user - ENDED");
        }
    }

    @GetMapping("/create-debug-user2")
    public ResponseEntity<String> createDebugUser() {
        try {
            customUserRepository.save(
                    new CustomUser(
                            "Frida",
                            "Smith",
                            "Frida@gmail.com",
                            passwordEncoder.encode("321"),
                            true,
                            true,
                            true,
                            true,
                            Set.of(UserRole.EMPLOYEE)
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully ");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists " + e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong " + e.getLocalizedMessage());
        } finally {
            System.out.println("Creating debug user - ENDED");
        }
    }

    @GetMapping("/who-am-i")
    public String whoAmI(Authentication authentication) {
        return "Hello " + authentication.getName() +
                "! Your roles: " + authentication.getAuthorities();
    }

    @GetMapping("/auth-session")
    public ResponseEntity<String> debugAuthenticationSession(Authentication authentication) {
        System.out.println(authentication.getClass().getSimpleName());
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication);

        return ResponseEntity.ok().body("Check logs");
    }

}
