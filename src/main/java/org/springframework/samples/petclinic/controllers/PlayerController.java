package org.springframework.samples.petclinic.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.configuration.jwt.JwtUtils;
import org.springframework.samples.petclinic.configuration.services.UserDetailsImpl;
import org.springframework.samples.petclinic.dto.EditPlayerDto;
import org.springframework.samples.petclinic.dto.JwtResponseDto;
import org.springframework.samples.petclinic.dto.LoginRequest;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.services.PlayerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/player")
@Tag(name = "Player", description = "Inicio de sesión, registro e información sobre los jugadores registrados en el sistema")
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

	@Operation(summary = "Inicia la sesión de un usuario")
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
		} catch (BadCredentialsException exception) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(summary = "Verifica la validez de un token JWT")
	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		if (isValid) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
	}

	@Operation(summary = "Devuelve el usuario que tiene iniciada la sesión actual")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/me")
	public ResponseEntity<Player> getMe() {
		Optional<Player> user = playerService.findCurrentPlayer();

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Registra a un usuario")
	@PostMapping("/signup")	
	public ResponseEntity<JwtResponseDto> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (playerService.existsUser(signUpRequest.getUsername()).equals(true)) {
			return new ResponseEntity<>(HttpStatus.IM_USED);
		} else {
			playerService.createUser(signUpRequest);

			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUsername(signUpRequest.getUsername());
			loginRequest.setPassword(signUpRequest.getPassword());

			return authenticateUser(loginRequest);
		}
	}

	@Operation(summary = "Edita correo, contraseña y nombre de usuario de un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@PatchMapping("/{id}")
	public ResponseEntity<Player> editUser(@PathVariable("id") Integer id, @Valid @RequestBody EditPlayerDto payload) {
		Optional<Player> user = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);

		if (!target.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user.isPresent()) {
			if (!user.get().getIs_admin()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			Player newPlayer = playerService.updatePlayer(payload, id);
			if (newPlayer != null) {
				return new ResponseEntity<>(newPlayer, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Operation(summary = "Edita correo, contraseña y nombre de usuario del usuario con sesión iniciada")
	@SecurityRequirement(name = "bearerAuth")
	@PatchMapping("/me")
	public ResponseEntity<Player> editMe(@Valid @RequestBody EditPlayerDto payload) {
		Optional<Player> user_opt = playerService.findCurrentPlayer();

		if (user_opt.isPresent()) {
			Player newPlayer = playerService.updatePlayer(payload, user_opt.get().getId());
			if (newPlayer != null) {
				return new ResponseEntity<>(newPlayer, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Elimina un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
		Optional<Player> user = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);

		if (!target.isPresent() || !user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!user.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		playerService.deletePlayer(id);
		return new ResponseEntity<>(HttpStatus.OK);		
	}

	@Operation(summary = "Añade un amigo a un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/friends/{id}/{friend_id}")	
	public ResponseEntity<?> addFriendToUser(@PathVariable("id") Integer id, @PathVariable("friend_id") Integer friend_id) {
		Optional<Player> currentUser = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);
		Optional<Player> friend = playerService.findPlayer(friend_id);

		if (!target.isPresent() || !friend.isPresent() || !currentUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!currentUser.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (playerService.areFriends(target.get(), friend.get())) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.addFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Elimina un amigo a un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/friends/{id}/{friend_id}")	
	public ResponseEntity<?> deleteFriendToUser(@PathVariable("id") Integer id, @PathVariable("friend_id") Integer friend_id) {
		Optional<Player> currentUser = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);
		Optional<Player> friend = playerService.findPlayer(friend_id);

		if (!target.isPresent() || !friend.isPresent() || !currentUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!currentUser.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (!playerService.areFriends(target.get(), friend.get())) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.removeFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Añade un amigo al usuario actual. Se proporciona el usuario del jugador a añadir como amigo")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/friends/{friend_username}")	
	public ResponseEntity<?> addFriendToMe(@PathVariable("friend_username") String friend_username) {
		Optional<Player> target = playerService.findCurrentPlayer();
		Optional<Player> friend = playerService.findByUsernamePlayer(friend_username);

		if (!target.isPresent() || !friend.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (playerService.areFriends(target.get(), friend.get())) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.addFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Elimina un amigo del usuario actual. Se proporciona el usuario del jugador a eliminar como amigo")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/friends/{friend_username}")	
	public ResponseEntity<?> deleteFriendToMe(@PathVariable("friend_username") String friend_username) {
		Optional<Player> target = playerService.findCurrentPlayer();
		Optional<Player> friend = playerService.findByUsernamePlayer(friend_username);

		if (!target.isPresent() || !friend.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!playerService.areFriends(target.get(), friend.get())) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.removeFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Lista todos los usuarios. El usuario de este método debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping
	public ResponseEntity<?> getAll() {
		Optional<Player> target = playerService.findCurrentPlayer();

		if (!target.isPresent() || !target.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<List<Player>> playerList = playerService.findAll();

		if (!playerList.isPresent() || (playerList.isPresent() && playerList.get().isEmpty())) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(playerList.get(), HttpStatus.OK);
	}
}
