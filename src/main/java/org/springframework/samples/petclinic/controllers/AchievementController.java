package org.springframework.samples.petclinic.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Achievement;
import org.springframework.samples.petclinic.services.AchievementService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/achievements")
@Tag(name = "Achievements", description = "The Achievements management API")
@SecurityRequirement(name = "bearerAuth")
public class AchievementController {
    
    private final AchievementService achievementService;

    @Autowired
	public AchievementController(AchievementService achievementService) {
		this.achievementService = achievementService;
	}

    @GetMapping
	public ResponseEntity<List<Achievement>> findAll() {
		return new ResponseEntity<>((List<Achievement>) achievementService.getAchievements(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Achievement> findAchievement(@PathVariable("id") int id){
		Achievement achievementToGet = achievementService.getById(id);

		if(achievementToGet == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Achievement>(achievementToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Achievement> createAchievement(@RequestBody @Valid Achievement newAchievement, BindingResult br){ 
		Achievement result = null;

		if(!br.hasErrors()) {
			result = achievementService.saveAchievement(newAchievement);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(result,HttpStatus.CREATED);	
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyAchievement(@RequestBody @Valid Achievement newAchievement, BindingResult br,@PathVariable("id") int id) {
		Achievement achievementToUpdate=this.findAchievement(id).getBody();
		if(br.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else if(newAchievement.getId()==null || !newAchievement.getId().equals(id))
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		else {
			BeanUtils.copyProperties(newAchievement, achievementToUpdate, "id");
			achievementService.saveAchievement(achievementToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAchievement(@PathVariable("id") int id){
		findAchievement(id);
		achievementService.deleteAchievementById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
