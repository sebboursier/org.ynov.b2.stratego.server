/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

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

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "team")
	private Set<Player> players;

}
