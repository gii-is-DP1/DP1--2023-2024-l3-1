package com.dobble.controllers;

import java.util.List;
import java.util.Optional;

import com.dobble.configuration.jwt.JwtUtils;
import com.dobble.configuration.services.UserDetailsImpl;
import com.dobble.dto.EditPlayerDto;
import com.dobble.dto.ExceptionMessageDto;
import com.dobble.dto.JwtResponseDto;
import com.dobble.dto.LoginRequestDto;
import com.dobble.dto.PublicPlayerDto;
import com.dobble.dto.SignupRequestDto;
import com.dobble.model.Player;
import com.dobble.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/player")
@RestControllerAdvice
@Tag(name = "Player", description = "Inicio de sesión, registro e información sobre los jugadores (usuarios) registrados en el sistema")
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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Inicio de sesión correcto", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class)) }),
			@ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Inicia la sesión de un usuario")
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Boolean isAdmin = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.count() > 0;

			return new ResponseEntity<>(new JwtResponseDto(jwt, userDetails.getId(), userDetails.getUsername(), isAdmin),
					HttpStatus.OK);
		} catch (BadCredentialsException exception) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "El token es correcto", content = @Content),
			@ApiResponse(responseCode = "406", description = "El token es incorrecto", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario actual", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) }),
			@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Devuelve el usuario que tiene iniciada la sesión actual")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/me")
	public ResponseEntity<?> getMe() {
		Optional<Player> user = playerService.findCurrentPlayer();

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "El token de inicio de sesión del usuario registrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class)) }),
			@ApiResponse(responseCode = "226", description = "Ya existe un usuario con ese nombre de usuario y correo electrónico", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Registra a un usuario")
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signUpRequest) {
		if (playerService.existsUser(signUpRequest.getUsername(), signUpRequest.getEmail())) {
			return new ResponseEntity<>(HttpStatus.IM_USED);
		} else {
			playerService.createUser(signUpRequest);

			LoginRequestDto loginRequest = new LoginRequestDto();
			loginRequest.setUsername(signUpRequest.getUsername());
			loginRequest.setPassword(signUpRequest.getPassword());

			return authenticateUser(loginRequest);
		}
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "El nuevo usuario", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) }),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra el usuario a modificar", content = @Content),
			@ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese e-mail o nombre de usuario", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Edita correo, contraseña y nombre de usuario de un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@PatchMapping("/{id}")
	public ResponseEntity<?> editUser(@PathVariable("id") Integer id, @Valid @RequestBody EditPlayerDto payload) {
		Optional<Player> user = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);

		if (!target.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (user.isPresent()) {
			if (!user.get().getIs_admin()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			if (playerService.existsUser(target.get(), payload)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}

			Player newPlayer = playerService.updatePlayer(payload, id);
			if (newPlayer != null) {
				return new ResponseEntity<>(newPlayer, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "El nuevo usuario", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) }),
			@ApiResponse(responseCode = "404", description = "No se encuentra el usuario a modificar", content = @Content),
			@ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese e-mail o nombre de usuario", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Edita correo, contraseña y nombre de usuario del usuario con sesión iniciada")
	@SecurityRequirement(name = "bearerAuth")
	@PatchMapping("/me")
	public ResponseEntity<?> editMe(@Valid @RequestBody EditPlayerDto payload) {
		Optional<Player> user_opt = playerService.findCurrentPlayer();

		if (user_opt.isPresent()) {
			if (playerService.existsUser(user_opt.get(), payload)) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}

			Player newPlayer = playerService.updatePlayer(payload, user_opt.get().getId());
			if (newPlayer != null) {
				return new ResponseEntity<>(newPlayer, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = @Content),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra el usuario a eliminar", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = @Content),
			@ApiResponse(responseCode = "304", description = "Los usuarios ya son amigos", content = @Content),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra alguno de los usuarios proporcionados", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Añade un amigo a un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/friends/{id}/{friend_username}")
	public ResponseEntity<?> addFriendToUser(@PathVariable("id") Integer id,
			@PathVariable("friend_username") String friend_username) {
		Optional<Player> currentUser = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);
		Optional<Player> friend = playerService.findByUsernamePlayer(friend_username);

		if (!target.isPresent() || !friend.isPresent() || !currentUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!currentUser.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (playerService.areFriends(target.get(), friend.get()) || target.get().getId() == friend.get().getId()) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.addFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Player.class, type = "array")) }),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra al usuario proporcionado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Lista los amigos de un usuario. El usuario que realice la petición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/friends/{id}")
	public ResponseEntity<?> getFriendsOfUser(@PathVariable("id") Integer id) {
		Optional<Player> currentUser = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);

		if (!target.isPresent() || !currentUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!currentUser.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(target.get().getFriends(), HttpStatus.OK);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PublicPlayerDto.class, type = "array")) }),
			@ApiResponse(responseCode = "401", description = "El usuario actual no ha iniciado sesión", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Lista los amigos del usuario actual")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/friends")
	public ResponseEntity<?> getFriendsOfMe() {
		Optional<Player> currentUser = playerService.findCurrentPlayer();

		if (!currentUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<PublicPlayerDto> public_friends_data = currentUser.get().getFriends()
				.stream()
				.map(p -> new PublicPlayerDto(p))
				.toList();

		return new ResponseEntity<>(public_friends_data, HttpStatus.OK);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = @Content),
			@ApiResponse(responseCode = "304", description = "Los usuarios no eran amigos", content = @Content),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra alguno de los usuarios proporcionados", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Elimina un amigo a un usuario. El usuario que realice la edición debe ser administrador")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/friends/{id}/{friend_username}")
	public ResponseEntity<?> deleteFriendToUser(@PathVariable("id") Integer id,
			@PathVariable("friend_username") String friend_username) {
		Optional<Player> currentUser = playerService.findCurrentPlayer();
		Optional<Player> target = playerService.findPlayer(id);
		Optional<Player> friend = playerService.findByUsernamePlayer(friend_username);

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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = @Content),
			@ApiResponse(responseCode = "304", description = "Los usuarios ya eran amigos", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra alguno de los usuarios proporcionados", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Añade un amigo al usuario actual. Se proporciona el usuario del jugador a añadir como amigo")
	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/friends/{friend_username}")
	public ResponseEntity<?> addFriendToMe(@PathVariable("friend_username") String friend_username) {
		Optional<Player> target = playerService.findCurrentPlayer();
		Optional<Player> friend = playerService.findByUsernamePlayer(friend_username);

		if (!target.isPresent() || !friend.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (playerService.areFriends(target.get(), friend.get()) || target.get().getId() == friend.get().getId()) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}

		playerService.addFriend(target.get(), friend.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = @Content),
			@ApiResponse(responseCode = "304", description = "Los usuarios no eran amigos", content = @Content),
			@ApiResponse(responseCode = "404", description = "No se encuentra alguno de los usuarios proporcionados", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class, type = "array")) }),
			@ApiResponse(responseCode = "204", description = "No hay usuarios que mostrar", content = @Content),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
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

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) }),
			@ApiResponse(responseCode = "401", description = "El usuario actual no es administrador", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
	@Operation(summary = "Obtiene un usuario por su ID")
	@GetMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> getById(@PathVariable int id) {
		Optional<Player> target = playerService.findCurrentPlayer();

		if (!target.isPresent() || !target.get().getIs_admin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<Player> playerList = playerService.findPlayer(id);

		if (!playerList.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(playerList.get(), HttpStatus.OK);
	}
}
