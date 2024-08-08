package kz.dossier.controller;

import jakarta.validation.Valid;
import kz.dossier.payload.request.LoginRequest;
import kz.dossier.payload.request.SignupRequest;
import kz.dossier.payload.response.JwtResponse;
import kz.dossier.payload.response.MessageResponse;
import kz.dossier.security.jwt.JwtUtils;
import kz.dossier.security.models.ERole;
import kz.dossier.security.models.Role;
import kz.dossier.security.models.User;
import kz.dossier.security.repository.RoleRepository;
import kz.dossier.security.repository.UserRepository;
import kz.dossier.security.services.UserDetailsImpl;
import kz.dossier.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3000)
@RestController
@RequestMapping("/api/pandora/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  @Autowired

  UserDetailsServiceImpl userDetailsService;
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsernameTwo(userDetails.getUsername()); // Assuming you have a method to get the User object
        Integer newTokenVersion = user.getTokenVersion() != null ? user.getTokenVersion() + 1 : 1; // Increment token version
        user.setTokenVersion(newTokenVersion);
        userRepository.save(user);

        String jwt = jwtUtils.generateJwtToken(authentication, newTokenVersion);
        String refreshToken = jwtUtils.generateRefreshToken(authentication, newTokenVersion);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
    
        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
  }

  @PostMapping("/refreshtoken")
public ResponseEntity<?> refreshToken(@Valid @RequestParam String request) {
    String requestRefreshToken = request;
    if (jwtUtils.validateJwtToken(requestRefreshToken)) {
        String username = jwtUtils.getUserNameFromJwtToken(requestRefreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        User user = userRepository.findByUsernameTwo(userDetails.getUsername()); // Assuming you have a method to get the User object
        Integer newTokenVersion = user.getTokenVersion() + 1; // Increment token version
        user.setTokenVersion(newTokenVersion);
        userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtUtils.generateJwtToken(authentication, newTokenVersion);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication, newTokenVersion);
        Map<String, String> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);
        return ResponseEntity.ok(result);
    } else {
        return ResponseEntity.badRequest().body("Invalid refresh token");
    }
}
@PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsernameTwo(userDetails.getUsername());
        // Increment token version to invalidate all previous tokens
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }

  @PostMapping("/changePassword")
  public void changePassword( @RequestParam String password, Principal principal, @RequestParam String username){
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      user.get().setPassword(encoder.encode(password));
      System.out.println(user.get().getUsername());
      System.out.println(user.get().getEmail());
      System.out.println(password);
      userRepository.save(user.get());
    }
  }
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getEmail(),
            signUpRequest.getUsername(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (signUpRequest.getLevel().equals("2")) {
      Role userRole = roleRepository.findByName(ERole.LEVEL_2_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);  }
    if (signUpRequest.getLevel().equals("1")) {
      Role userRole = roleRepository.findByName(ERole.LEVEL_1_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);  }if (signUpRequest.getLevel().equals("3")) {
      Role userRole = roleRepository.findByName(ERole.LEVEL_3_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);  }if (signUpRequest.getLevel().equals("vip")) {
      Role userRole = roleRepository.findByName(ERole.VIP)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);  }if (signUpRequest.getLevel().equals("admin")) {
      Role userRole = roleRepository.findByName(ERole.ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);  }
    user.setActive(true);
    user.setRoles(roles);
    userRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
