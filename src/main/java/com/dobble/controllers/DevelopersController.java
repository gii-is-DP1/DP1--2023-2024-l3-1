package com.dobble.controllers;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.maven.model.Developer;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dobble.dto.ExceptionMessageDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/developers")
@Tag(name = "Developers", description = "Información sobre los desarrolladores del sistema")
public class DevelopersController {

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud correcta", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Developer.class)) }),
            @ApiResponse(responseCode = "500", description = "Error desconocido del servidor", content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageDto.class)) }) })
    @Operation(summary = "Obtiene una lista de los desarrolladores de la aplicación")
    public ResponseEntity<List<Developer>> getDevelopers() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml", StandardCharsets.UTF_8));
            return new ResponseEntity<>(model.getDevelopers(), HttpStatus.OK);
        } catch (IOException | XmlPullParserException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
