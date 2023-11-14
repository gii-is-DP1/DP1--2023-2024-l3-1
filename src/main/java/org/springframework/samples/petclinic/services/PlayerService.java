package org.springframework.samples.petclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.samples.petclinic.repositories.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

    @Transactional
	public void createUser(@Valid SignupRequest request) {
		Player player = new Player();
		player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
		player.setPassword(encoder.encode(request.getPassword()));
		this.repository.save(player);
    }

	public Boolean existsUser(String username) {
		return this.repository.findByUsername(username).get().getUsername().equals(username);
	}
}
