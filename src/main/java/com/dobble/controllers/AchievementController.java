package com.dobble.controllers;

import java.util.List;
import java.util.Optional;

import com.dobble.dto.ExceptionMessageDto;
import com.dobble.model.Achievement;
import com.dobble.model.Player;
import com.dobble.services.AchievementService;
import com.dobble.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RestControllerAdvice
@RequestMapping("/api/achievements")
@Tag(name = "Achievements", description = "Gestión y listado de los logros del juego")
@SecurityRequirement(name = "bearerAuth")
public class AchievementController {

    private final AchievementService achievementService;
    private final PlayerService playerService;

    @Autowired
    public AchievementController(AchievementService achievementService, PlayerService playerService) {
        this.achievementService = achievementService;
        this.playerService = playerService;
    }

    @GetMapping
    @Operation(summary = "Obtiene una lista de todos los logros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class, type = "array")) }),
            @ApiResponse(responseCode = "204", description = "No hay contenido que mostrar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) })
    })
    public ResponseEntity<List<Achievement>> findAll() {
        Optional<List<Achievement>> optionalAchievements = achievementService.getAchievements();

        if (optionalAchievements.isPresent()) {
            List<Achievement> achievements = optionalAchievements.get();
            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un logro a partir de su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class)) }),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra el logro solicitado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) })
    })
    public ResponseEntity<?> findAchievement(@PathVariable("id") int id) {
        Optional<Achievement> achievementToGet = achievementService.getAchievementById(id);
        if (!achievementToGet.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Achievement>(achievementToGet.get(), HttpStatus.OK);
    }

    @PostMapping("/new")
    @Operation(summary = "Crea un logro. El usuario que realiza la creación debe ser administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Logro creado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class)) }),
            @ApiResponse(responseCode = "400", description = "Los datos proporcionados no han pasado la validación", content = @Content),
            @ApiResponse(responseCode = "401", description = "El jugador actual no está autenticado o no es administrador", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
			    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Achievement> createAchievement(@RequestBody @Valid Achievement newAchievement,
            BindingResult br) {
        Optional<Player> user = playerService.findCurrentPlayer();
        Achievement result = null;

        if (user.isPresent() && !user.get().getIs_admin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!br.hasErrors()) {
            result = achievementService.saveAchievement(newAchievement);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edita nombre, descripción, imagen, métrica y umbral de un logro. El usuario que realiza la edición debe ser administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class)) }),
            @ApiResponse(responseCode = "401", description = "El usuario actual no está autorizado para realizar esta operación.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra el logro a actualizar.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Achievement> modifyAchievement(@PathVariable("id") Integer id,
            @Valid @RequestBody Achievement payload) {
        Optional<Player> user = playerService.findCurrentPlayer();
        Optional<Achievement> target = achievementService.getAchievementById(id);

        if (!target.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.isPresent()) {
            if (!user.get().getIs_admin()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Achievement newAchievement = achievementService.updateAchievement(payload, id);
            if (newAchievement != null) {
                return new ResponseEntity<>(newAchievement, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un logro. El usuario que realice la creación debe ser administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación realizada correctamente.", content = @Content),
            @ApiResponse(responseCode = "401", description = "El usuario actual no está autorizado para realizar esta operación.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra el logro a eliminar.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor.", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAchievement(@PathVariable("id") int id) {
        Optional<Player> optionalCurrentPlayer = playerService.findCurrentPlayer();
        if (!optionalCurrentPlayer.isPresent() || !optionalCurrentPlayer.get().getIs_admin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Achievement> optionalAchievement = achievementService.getAchievementById(id);
        if (!optionalAchievement.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        achievementService.deleteAchievementById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
