package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DobbleAuthoritiesService {
    private DobbleAuthoritiesRepository authoritiesRepository; 


    @Autowired
    public DobbleAuthoritiesService(DobbleAuthoritiesRepository authoritiesRepository){
        this.authoritiesRepository=authoritiesRepository; 
    }
    
}
