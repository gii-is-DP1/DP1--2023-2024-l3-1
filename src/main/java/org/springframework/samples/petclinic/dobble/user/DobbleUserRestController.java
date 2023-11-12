package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.util.RestPreconditions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dobbleusers")
@SecurityRequirement(name = "bearerAuth")
public class DobbleUserRestController {

    private final DobbleUserService userService;
	private final DobbleAuthoritiesService authService; 

    @Autowired
	public DobbleUserRestController(DobbleUserService userService, DobbleAuthoritiesService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<DobbleUser> findById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DobbleUser> create(@RequestBody @Valid DobbleUser user) {
		DobbleUser savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	
	@PutMapping(value = "{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DobbleUser> update(@PathVariable("userId") Integer id, @RequestBody @Valid DobbleUser user) {
		RestPreconditions.checkNotNull(userService.findUser(id), "DobbleUser", "ID", id);
		return new ResponseEntity<>(this.userService.updateUser(user, id), HttpStatus.OK);
	}
	 
	

	@GetMapping("current")
	public ResponseEntity<DobbleUser> findCurrent(){
		DobbleUser currentUser = userService.findCurrentDobbleUser();
		return new ResponseEntity<>(currentUser,HttpStatus.OK);
	}
    
}
