package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthoritiesService {
    private AuthoritiesRepository authoritiesRepository; 


    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository){
        this.authoritiesRepository=authoritiesRepository; 
    }
    
}
