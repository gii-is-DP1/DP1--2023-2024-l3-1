package org.springframework.samples.petclinic.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Developer;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class DevelopersControllerTest {

  @Mock
  private MavenXpp3Reader readerMock;

  @InjectMocks
  private DevelopersController developersController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @WithMockUser(username = "admin", password = "dobble_admin")
  public void testGetDevelopers_Success() throws IOException, XmlPullParserException {
    ResponseEntity<List<Developer>> responseEntity = developersController.getDevelopers();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
