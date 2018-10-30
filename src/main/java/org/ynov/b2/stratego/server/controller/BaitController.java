/**
 *
 */
package org.ynov.b2.stratego.server.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sebboursier
 *
 */
@RestController
public class BaitController {

	@RequestMapping("QZFHQBZDILUHQLIGDILZDGHLSFHKHEGBLQGZdfg")
	public HttpEntity<?> etienneBait() {
		return new ResponseEntity<>("Coucou étienne", HttpStatus.OK);
	}

}
