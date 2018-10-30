/**
 *
 */
package org.ynov.b2.stratego.server.jpa.util;

import java.util.UUID;

import javax.persistence.PrePersist;

import org.ynov.b2.stratego.server.jpa.model.Team;

/**
 * @author sebboursier
 *
 */
public class TeamListener {

	@PrePersist
	public void setLastUpdate(Team team) {
		if (team.getUuid() == null) {
			team.setUuid(UUID.randomUUID().toString());
		}
	}

}
