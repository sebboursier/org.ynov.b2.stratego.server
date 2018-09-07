/**
 *
 */
package org.ynov.b2.stratego.server.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
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

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "game")
	private Set<Player> players;

	private Date dateStarted;

	private Date dateEnded;

	@OneToOne
	private Player winner;

	@Lob
	@Convert(converter = JsonArrayConverter.class)
	private Pion[][] board;
}
