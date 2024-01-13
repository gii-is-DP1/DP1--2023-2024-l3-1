package org.springframework.samples.petclinic.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.EditPlayerDto;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.model.enums.Icon;
import org.springframework.samples.petclinic.repositories.PlayerRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class PlayerService {
	private final PasswordEncoder encoder;
	private final PlayerRepository repository;

	@Autowired
	public PlayerService(PasswordEncoder encoder, PlayerRepository repository) {
		this.encoder = encoder;
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public Optional<Player> findPlayer(Integer id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<Player> findCurrentPlayer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return this.repository.findByUsername(auth.getName());
	}

	@Transactional(readOnly = true)
	public Optional<Player> findByUsernamePlayer(String username) {
		return repository.findByUsername(username);
	}

	// Nos coge todos los usuarios que no son admin
	@Transactional(readOnly = true)
	public Optional<List<Player>> findAll() {
		return repository.findAllNonAdmin();
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		Player player = new Player();
		player.setUsername(request.getUsername().toLowerCase());
		player.setEmail(request.getEmail().toLowerCase());
		player.setPassword(encoder.encode(request.getPassword()));
		player.setProfile_icon(request.getProfile_icon());
		this.repository.save(player);
	}

	@Transactional
	public Player updatePlayer(@Valid EditPlayerDto payload, Integer idToUpdate) {
		Optional<Player> toUpdate_opt = findPlayer(idToUpdate);

		if (toUpdate_opt.isPresent()) {
			Player toUpdate = toUpdate_opt.get();
			String newUsername = payload.getUsername();
			String newEmail = payload.getEmail();
			String newPassword = payload.getPassword();
			Icon newProfileIcon = payload.getProfile_icon();
			if (newUsername != null && !newUsername.isBlank()) {
				toUpdate.setUsername(newUsername.toLowerCase());
			}
			if (newEmail != null && !newEmail.isBlank()) {
				toUpdate.setEmail(newEmail.toLowerCase());
			}
			if (newPassword != null && !newPassword.isBlank()) {
				toUpdate.setPassword(encoder.encode(newPassword));
			}
			if (newProfileIcon != null) {
				toUpdate.setProfile_icon(newProfileIcon);
			}

			this.repository.save(toUpdate);

			return toUpdate;
		}

		return null;
	}

	@Transactional
	public void addFriend(Player target, Player friend) {
		target.getFriends().add(friend);
		this.repository.save(target);
	}

	@Transactional
	public void removeFriend(Player target, Player friend) {
		target.getFriends().remove(friend);
		this.repository.save(target);
	}

	@Transactional(readOnly = true)
	public Boolean areFriends(Player target, Player friend) {
		return target.getFriends().contains(friend);
	}

	@Transactional
	public void deletePlayer(Integer idToDelete) {
		Optional<Player> toDelete_opt = findPlayer(idToDelete);

		if (toDelete_opt.isPresent()) {
			this.repository.delete(toDelete_opt.get());
		}
	}

	public Boolean existsUser(String username, String email) {
		Optional<Player> op_usernamePlayer = this.repository.findByUsername(username);
		Optional<Player> op_emailPlayer = this.repository.findByEmail(email);

		return op_usernamePlayer.isPresent() || op_emailPlayer.isPresent();
	}

	public Boolean existsUser(Player targetPlayer, EditPlayerDto editPayload) {
		Optional<Player> op_usernamePlayer = this.repository.findByUsername(editPayload.getUsername());
		Optional<Player> op_emailPlayer = this.repository.findByEmail(editPayload.getEmail());

		return (op_usernamePlayer.isPresent() && op_usernamePlayer.get().getId() != targetPlayer.getId()) ||
			(op_emailPlayer.isPresent() && op_emailPlayer.get().getId() != targetPlayer.getId()) ;
	}
}
