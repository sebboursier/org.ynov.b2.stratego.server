/**
 *
 */
package org.ynov.b2.stratego.server.redis.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ynov.b2.stratego.server.redis.model.TeamInLobby;

/**
 * @author sebboursier
 *
 */
@Repository
public interface TeamInLobbyRepository extends CrudRepository<TeamInLobby, String> {

	public default TeamInLobby getFirst() {
		List<TeamInLobby> avatarInLobbies = (List<TeamInLobby>) this.findAll();
		avatarInLobbies.sort((TeamInLobby o1, TeamInLobby o2) -> {
			if (o1 != null && o2 != null) {
				return o1.getDateCreated().compareTo(o2.getDateCreated());
			} else if (o1 != null) {
				return -1;
			}
			return 1;
		});
		if (avatarInLobbies.size() > 0) {
			return avatarInLobbies.get(0);
		}
		return null;
	}
}
