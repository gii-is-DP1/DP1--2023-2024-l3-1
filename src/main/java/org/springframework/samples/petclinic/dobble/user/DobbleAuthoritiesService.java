package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DobbleAuthoritiesService {
    private DobbleAuthoritiesRepository authoritiesRepository; 


    @Autowired
    public DobbleAuthoritiesService(DobbleAuthoritiesRepository authoritiesRepository){
        this.authoritiesRepository=authoritiesRepository; 
    }
    
    @Transactional(readOnly = true)
	public Iterable<DobbleAuthorities> findAll() {
		return this.authoritiesRepository.findAll();
	}

}
