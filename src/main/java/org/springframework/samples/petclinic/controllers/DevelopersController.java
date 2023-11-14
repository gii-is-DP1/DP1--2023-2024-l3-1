package org.springframework.samples.petclinic.controllers;

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

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/developers")
@Tag(name = "Developers", description = "Información sobre los desarrolladores del sistema")
public class DevelopersController {
    @GetMapping
    public ResponseEntity<List<Developer>> getDevelopers(){
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml", StandardCharsets.UTF_8));
            return new ResponseEntity<>(model.getDevelopers(), HttpStatus.OK);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }      
    }
}
