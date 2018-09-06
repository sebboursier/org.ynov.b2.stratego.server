/**
 *
 */
package org.ynov.b2.stratego.server.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.ynov.b2.stratego.server.config.JsonArrayConverter;

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
public class Player extends SuperEntity {

	private int index;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Team team;

	@Column
	@Convert(converter = JsonArrayConverter.class)
	private Pions[][] pions;

	@OneToMany(mappedBy = "player")
	private Set<Move> moves;

}
