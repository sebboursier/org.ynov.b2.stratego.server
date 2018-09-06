/**
 *
 */
package org.ynov.b2.stratego.server.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
public class Game extends SuperEntity {

	@OneToMany(mappedBy = "game")
	private Set<Player> players;

	private Date dateStarted;

	private Date dateEnded;

	@OneToOne
	private Player winner;

	@Column
	@Convert(converter = JsonArrayConverter.class)
	private Pions[][] board;
}
