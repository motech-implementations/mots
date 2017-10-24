package org.motechproject.mots.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Hello {

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public ResponseEntity<?> hello() {
    return new ResponseEntity<>("hello", HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @RequestMapping(value = "/secured", method = RequestMethod.GET)
  public ResponseEntity<?> secured() {
    return new ResponseEntity<>("secured", HttpStatus.OK);
  }
}

