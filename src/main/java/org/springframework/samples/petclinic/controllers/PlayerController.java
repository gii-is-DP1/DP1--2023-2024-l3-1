package org.springframework.samples.petclinic.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.configuration.jwt.JwtUtils;
import org.springframework.samples.petclinic.configuration.services.UserDetailsImpl;
import org.springframework.samples.petclinic.dobble.user.DobbleUserService;
import org.springframework.samples.petclinic.dto.JwtResponseDto;
import org.springframework.samples.petclinic.dto.LoginRequest;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.services.PlayerService;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/players")
@Tag(name = "Players", description = "Inicio de sesión, registro e información sobre los jugadores registrados en el sistema")
public class PlayerController {

    private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final PlayerService playerService;

	@Autowired
	public PlayerController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PlayerService playerService) {
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.playerService = playerService;
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try{
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Boolean isAdmin = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.count() > 0;

			return new ResponseEntity<>(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getUsername(), isAdmin), HttpStatus.OK);
		}catch(BadCredentialsException exception){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		return new ResponseEntity<>(isValid, HttpStatus.OK);
	}

	
	@PostMapping("/signup")	
	public ResponseEntity<JwtResponseDto> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (playerService.existsUser(signUpRequest.getUsername()).equals(true)) {
			return new ResponseEntity<>(HttpStatus.IM_USED);
		}
		playerService.createUser(signUpRequest);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername(signUpRequest.getUsername());
		loginRequest.setPassword(signUpRequest.getPassword());

		return authenticateUser(loginRequest);
	}
    
}
