/**
 *
 */
package org.ynov.b2.stratego.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ynov.b2.stratego.server.model.Move;

/**
 * @author sebboursier
 *
 */
@RepositoryRestResource
public interface MoveRepository extends JpaRepository<Move, Integer> {

}
