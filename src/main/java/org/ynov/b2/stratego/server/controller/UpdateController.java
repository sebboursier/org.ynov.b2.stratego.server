/**
 *
 */
package org.ynov.b2.stratego.server.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;

/**
 * @author sebboursier
 *
 */
@RestController
@RequestMapping("update")
public class UpdateController {

	@Autowired
	private TeamRepository teamRepository;

	@RequestMapping("generateUUID")
	public HttpEntity<?> generateUUID(@RequestParam("ids") int[] ids) {
		for (int id : ids) {
			final Team team = teamRepository.getOne(id);
			team.setUuid(UUID.randomUUID());
			teamRepository.save(team);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
}
