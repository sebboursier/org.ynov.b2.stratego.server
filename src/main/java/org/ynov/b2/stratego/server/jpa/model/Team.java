/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
public class Team {

	private static final long serialVersionUID = 1L;

	@Id
	protected Integer id;

	private String uuid;

	@OneToMany(mappedBy = "team")
	private Set<Player> players;

	@OneToMany(mappedBy = "team", cascade = { CascadeType.ALL })
	private Set<Etudiant> etudiants;

	private String name;

	@Enumerated(EnumType.STRING)
	private TeamGroupe groupe;

	public Team(final Integer id, final TeamGroupe groupe, final Etudiant... etudiants) {
		this.id = id;
		this.groupe = groupe;
		this.etudiants = new HashSet<>(Arrays.asList(etudiants));
	}

	public Team(final Integer id, final TeamGroupe groupe, final String name) {
		this.id = id;
		this.groupe = groupe;
		this.name = name;
	}
}
