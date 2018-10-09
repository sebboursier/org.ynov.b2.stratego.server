/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import org.ynov.b2.stratego.server.jpa.util.TeamListener;

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
@EntityListeners(TeamListener.class)
public class Team extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;

	@OneToMany(mappedBy = "team")
	private Set<Player> players;

	private String name;

	private String groupe;

	private String studentOne;

	private String studentTwo;
}
