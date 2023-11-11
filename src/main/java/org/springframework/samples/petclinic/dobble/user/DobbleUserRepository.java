package org.springframework.samples.petclinic.dobble.user;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface DobbleUserRepository extends CrudRepository<DobbleUser,Integer>{
    

    Optional<DobbleUser> findByUsername(String username);


   

    
}
