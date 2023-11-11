package org.springframework.samples.petclinic.dobble.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DobbleUser> create(@RequestBody @Valid DobbleUser user) {
		DobbleUser savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@GetMapping("current")
	public ResponseEntity<DobbleUser> findCurrent(){
		DobbleUser currentUser = userService.findCurrentDobbleUser();
		return new ResponseEntity<>(currentUser,HttpStatus.OK);
	}
    
}
