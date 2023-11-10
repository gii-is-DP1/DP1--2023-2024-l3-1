package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepository; 


    @Autowired
    public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


    @Transactional
	public User saveUser(User user) throws DataAccessException {
		userRepository.save(user);
		return user;
	}

}