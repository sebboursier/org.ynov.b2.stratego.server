/**
 *
 */
package org.ynov.b2.stratego.server.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ynov.b2.stratego.server.jpa.model.Player;

/**
 * @author sebboursier
 *
 */
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Integer> {

	Player findByGameIdAndTeamUuid(final @Param("gameId") Integer gameId, final @Param("teamUuid") String teamUuid);

}
