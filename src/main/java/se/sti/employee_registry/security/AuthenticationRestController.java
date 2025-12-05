package se.sti.employee_registry.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.sti.employee_registry.config.RabbitConfig;
import se.sti.employee_registry.security.jwt.JwtUtils;
import se.sti.employee_registry.user.CustomUserDetails;
import se.sti.employee_registry.user.dto.CustomUserLoginDTO;
import se.sti.employee_registry.user.mapper.CustomUserMapper;

import java.util.Map;

@RestController
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AmqpTemplate amqpTemplate;
    private final CustomUserMapper customUserMapper;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AmqpTemplate amqpTemplate, CustomUserMapper customUserMapper){
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.amqpTemplate = amqpTemplate;
        this.customUserMapper = customUserMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody CustomUserLoginDTO customUserLoginDTO, HttpServletResponse response
    ) {
        logger.debug("Attempted authentication for user {}", customUserLoginDTO.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserLoginDTO.email(),
                        customUserLoginDTO.password()
                )
        );

        System.out.println("\n========= AUTHENTICATION RESULT =========");
        System.out.println("Class: " + authentication.getClass().getSimpleName());
        System.out.println("Authenticated: " + authentication.isAuthenticated());

        Object principal = authentication.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getSimpleName());
        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("  Email: " + userDetails.getCustomUserDTO().email());
            System.out.println("  Authorities: " + userDetails.getAuthorities());
            System.out.println("  Account Non Locked: " + userDetails.isAccountNonLocked());
            System.out.println("  Account Enabled: " + userDetails.isEnabled());
            System.out.println("  Password (hashed): " + userDetails.getPassword());
        } else {
            System.out.println("Principal value: " + principal);
        }

        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("=========================================\n");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateJwtToken(customUserMapper.detailDTOToEntity(customUserDetails.getCustomUserDTO()));

        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ✅ change to true in production (HTTPS only)
        cookie.setAttribute("SameSite", "Lax");
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hour
        response.addCookie(cookie);

        logger.info("Authentication successful for user: {}", customUserLoginDTO.email());

        amqpTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                "User Logged in, todo: send email to user to alert them of login from weird IP addresses"
        );

        return ResponseEntity.ok(Map.of(
                "username", customUserLoginDTO.email(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }

}
