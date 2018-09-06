/**
 *
 */
package org.ynov.b2.stratego.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ynov.b2.stratego.server.model.Player;

/**
 * @author sebboursier
 *
 */
public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
