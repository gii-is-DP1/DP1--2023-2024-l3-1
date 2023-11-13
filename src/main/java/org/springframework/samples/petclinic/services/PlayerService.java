package org.springframework.samples.petclinic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.SignupRequest;
import org.springframework.samples.petclinic.model.Player;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class PlayerService {
    private final PasswordEncoder encoder;

	@Autowired
	public PlayerService(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

    @Transactional
	public void createUser(@Valid SignupRequest request) {
		Player player = new Player();
		player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
		player.setPassword(encoder.encode(request.getPassword()));
    }
}
