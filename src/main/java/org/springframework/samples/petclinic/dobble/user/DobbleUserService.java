package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DobbleUserService {
    private DobbleUserRepository dobbleUserRepository; 


    @Autowired
    public DobbleUserService(DobbleUserRepository dobbleUserRepository) {
		this.dobbleUserRepository = dobbleUserRepository;
	}


    @Transactional
	public DobbleUser saveDobbleUser(DobbleUser user) throws DataAccessException {
		dobbleUserRepository.save(user);
		return user;
	}

	@Transactional(readOnly = true)
	public DobbleUser findCurrentDobbleUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return dobbleUserRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("DobbleUser", "Username", auth.getName()));
	}

	@Transactional(readOnly = true)
	public Iterable<DobbleUser> findAll() throws DataAccessException {
		return dobbleUserRepository.findAll();
	}

	
	@Transactional(readOnly = true)
	public DobbleUser findDobbleUserById(int id) throws DataAccessException {
		return this.dobbleUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("DobbleUser", "ID", id));
	}

	@Transactional
	public DobbleUser updatDobbleUser(DobbleUser dobbleUser, int id) throws DataAccessException {
		DobbleUser toUpdate = findDobbleUserById(id);
		BeanUtils.copyProperties(dobbleUser, toUpdate, "id", "user");
		return saveDobbleUser(toUpdate);
	}

	@Transactional
	public void deleteDobbleUser(int id) throws DataAccessException {
		DobbleUser toDelete = findDobbleUserById(id);
		dobbleUserRepository.delete(toDelete);
	}

}