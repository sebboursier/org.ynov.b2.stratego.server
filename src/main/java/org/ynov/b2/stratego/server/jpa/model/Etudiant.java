/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
public class Etudiant extends SuperEntity {

	private static final long serialVersionUID = 1L;

	private String nom;

	private String prenom;

	private String email;

	@ManyToOne
	private Team team;

	public Etudiant(final String nom, final String prenom, final String email) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
	}
}
