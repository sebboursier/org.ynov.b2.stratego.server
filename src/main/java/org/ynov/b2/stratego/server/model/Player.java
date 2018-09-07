/**
 *
 */
package org.ynov.b2.stratego.server.model;

import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
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

	private static final long serialVersionUID = 1L;

	private int num;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Team team;

	@Lob
	@Convert(converter = JsonArrayConverter.class)
	private PionType[][] pions;

	@OneToMany(mappedBy = "player")
	private Set<Move> moves;

}
