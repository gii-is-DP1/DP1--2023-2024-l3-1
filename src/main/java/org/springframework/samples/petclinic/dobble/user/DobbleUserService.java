package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DobbleUserService {
    private DobbleUserRepository userRepository; 


    @Autowired
    public DobbleUserService(DobbleUserRepository userRepository) {
		this.userRepository = userRepository;
	}


    @Transactional
	public DobbleUser saveUser(DobbleUser user) throws DataAccessException {
		userRepository.save(user);
		return user;
	}

		@Transactional(readOnly = true)
	public DobbleUser findCurrentDobbleUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return userRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("DobbleUser", "Username", auth.getName()));
	}


}