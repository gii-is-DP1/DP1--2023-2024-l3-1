package com.dobble.configuration.services;

import com.dobble.model.Player;
import com.dobble.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	PlayerRepository playerRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Player user = playerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

		return UserDetailsImpl.build(user);
	}

	@Transactional
	public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
		Player user = playerRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

		return UserDetailsImpl.build(user);
	}
}
