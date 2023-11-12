package org.springframework.samples.petclinic.dobble.user;


import java.util.Optional;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DobbleUserRepository extends CrudRepository<DobbleUser,Integer>{
    

    Optional<DobbleUser> findByUsername(String username);

    @Query("SELECT DISTINCT dobbleUser FROM DobbleUser dobbleUser WHERE dobbleUser.user.id = :userId")
    public Optional<DobbleUser> findByUser(int userId);



   
    
    
}
