package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

}