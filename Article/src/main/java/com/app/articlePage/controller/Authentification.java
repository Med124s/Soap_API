package com.app.articlePage.controller;

import com.app.articlePage.Model.ERoles;
import com.app.articlePage.Model.URoles;
import com.app.articlePage.Model.User;
import com.app.articlePage.Security.jwt.JwtCreate;
import com.app.articlePage.Security.service.Service.UserDetailImp;
import com.app.articlePage.payload.request.LoginRequest;
import com.app.articlePage.payload.request.SignUpRequest;
import com.app.articlePage.payload.response.JwtResponse;
import com.app.articlePage.payload.response.MessageResponse;
import com.app.articlePage.repository.RoleRepository;
import com.app.articlePage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")

public class Authentification {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtCreate jwtCreate;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("********userName "+loginRequest.getUsername());
        System.out.println("********Password "+loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCreate.generateJwtToken(authentication);

        UserDetailImp userDetails = (UserDetailImp) authentication.getPrincipal();
        //get roles

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        System.out.println("*** roles is "+roles);
        if(roles.isEmpty()){
            System.out.println("NOO ROLE IN THIS USER");
            return ResponseEntity.badRequest().body(new MessageResponse("User has no Access to application"));
        }
        else

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy hh:mm").format(Calendar.getInstance().getTime());
        signUpRequest.setCreatedAt(timeStamp);
        signUpRequest.setUpdatedAt(timeStamp);
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getCreatedAt(),
                signUpRequest.getUpdatedAt()
                );

        Set<String> strRoles = signUpRequest.getRole();
        Set<URoles> roles = new HashSet<>();

        if (strRoles == null) {
            System.out.println("No Role ****************");
            URoles userRole = roleRepository.findByName(ERoles.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        URoles adminRole = roleRepository.findByName(ERoles.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        URoles userRole = roleRepository.findByName(ERoles.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
