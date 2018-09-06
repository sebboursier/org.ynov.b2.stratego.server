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
@RequestMapping("test")
public class TestController {

	@RequestMapping("sandBox")
	public HttpEntity<?> sandBox() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
