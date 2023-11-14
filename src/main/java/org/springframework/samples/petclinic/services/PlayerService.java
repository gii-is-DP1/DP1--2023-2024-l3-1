package org.springframework.samples.petclinic.services;

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

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		Player player = new Player();
		player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
		player.setPassword(encoder.encode(request.getPassword()));
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
			Icon newProfileIcon = payload.getProfileIcon();
			if (newUsername != null && !newUsername.isBlank()) {
				toUpdate.setUsername(payload.getUsername());
			}
			if (newEmail != null && !newEmail.isBlank()) {
				toUpdate.setEmail(payload.getEmail());
			}
			if (newPassword != null && !newPassword.isBlank()) {
				toUpdate.setPassword(encoder.encode(payload.getPassword()));
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
	public void deletePlayer(Integer idToDelete) {
		Optional<Player> toDelete_opt = findPlayer(idToDelete);

		if (toDelete_opt.isPresent()) {
			this.repository.delete(toDelete_opt.get());
		}
	}

	public Boolean existsUser(String username) {
		return this.repository.findByUsername(username).get().getUsername().equals(username);
	}
}
