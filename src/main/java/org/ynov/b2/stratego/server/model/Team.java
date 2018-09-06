/**
 *
 */
package org.ynov.b2.stratego.server.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sebboursier
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Team extends SuperEntity {

	@OneToMany(mappedBy = "team")
	private Set<Player> players;

}
