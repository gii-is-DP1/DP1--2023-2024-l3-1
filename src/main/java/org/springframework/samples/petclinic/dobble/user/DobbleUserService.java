package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

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
	public DobbleUser findUser(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("DobbleUser", "id", id));
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

	@Transactional
	public DobbleUser updateUser(@Valid DobbleUser user, Integer idToUpdate) {
		DobbleUser toUpdate = findUser(idToUpdate);
		BeanUtils.copyProperties(user, toUpdate, "id");
		userRepository.save(toUpdate);

		return toUpdate;
	}


}