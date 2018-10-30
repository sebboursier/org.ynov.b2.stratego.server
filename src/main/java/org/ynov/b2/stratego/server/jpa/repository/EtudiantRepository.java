/**
 *
 */
package org.ynov.b2.stratego.server.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ynov.b2.stratego.server.jpa.model.Etudiant;

/**
 * @author sebboursier
 *
 */
@RepositoryRestResource
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

}
