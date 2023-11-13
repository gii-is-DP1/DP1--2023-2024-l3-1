package org.springframework.samples.petclinic.dobble.user;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.auth.payload.response.MessageResponse;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.util.RestPreconditions;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/dobbleUsers")
@SecurityRequirement(name = "bearerAuth")
public class DobbleUserRestController {

    private final DobbleUserService dobbleUserService;
	private final UserService userService;


    @Autowired
	public DobbleUserRestController(DobbleUserService dobbleUserService, UserService userService) {
		this.userService = userService;
		this.dobbleUserService = dobbleUserService;

	}
	
	@GetMapping
	public ResponseEntity<List<DobbleUser>> findAll() {
		return new ResponseEntity<>((List<DobbleUser>) dobbleUserService.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "{dobbleUserId}")
	public ResponseEntity<DobbleUser> findById(@PathVariable("dobbleUserId") int id) {
		return new ResponseEntity<>(dobbleUserService.findDobbleUserById(id), HttpStatus.OK);
	}
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<DobbleUser> create(@RequestBody @Valid DobbleUser dobbleUser) throws URISyntaxException {
		DobbleUser newDobbleUser = new DobbleUser();
		BeanUtils.copyProperties(dobbleUser, newDobbleUser, "id");
		User user = userService.findCurrentUser();
		newDobbleUser.setUser(user);
		DobbleUser savedDobbleUser = this.dobbleUserService.saveDobbleUser(newDobbleUser);

		return new ResponseEntity<>(savedDobbleUser, HttpStatus.CREATED);
	}


	@PutMapping(value = "{dobbleUserId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<DobbleUser> update(@PathVariable("dobbleUserId") int dobbleUserId, @RequestBody @Valid DobbleUser dobbleUser) {
		RestPreconditions.checkNotNull(dobbleUserService.findDobbleUserById(dobbleUserId), "DobbleUser", "ID", dobbleUserId);
		return new ResponseEntity<>(this.dobbleUserService.updatDobbleUser(dobbleUser, dobbleUserId), HttpStatus.OK);
	}

	@DeleteMapping(value = "{dobbleUserId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> delete(@PathVariable("dobbleUserId") int id) {
		RestPreconditions.checkNotNull(dobbleUserService.findDobbleUserById(id), "DobbleUser", "ID", id);
		dobbleUserService.deleteDobbleUser(id);
		return new ResponseEntity<>(new MessageResponse("DobbleUser deleted!"), HttpStatus.OK);
	}


/* 
	@GetMapping("current")
	public ResponseEntity<DobbleUser> findCurrent(){
		DobbleUser currentUser = userService.findCurrentUser();
		return new ResponseEntity<>(currentUser,HttpStatus.OK);
	}*/
    
}
