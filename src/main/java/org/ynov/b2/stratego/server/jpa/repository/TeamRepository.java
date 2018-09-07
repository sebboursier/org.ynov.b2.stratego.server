/**
 *
 */
package org.ynov.b2.stratego.server.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ynov.b2.stratego.server.jpa.model.Team;

/**
 * @author sebboursier
 *
 */
@RepositoryRestResource
public interface TeamRepository extends JpaRepository<Team, Integer> {

}
