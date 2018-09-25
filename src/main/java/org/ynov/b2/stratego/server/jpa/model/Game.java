/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.ynov.b2.stratego.server.util.converter.PionArrayConverter;

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

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
	private Set<Player> players;

	private Date dateStarted;

	private Date dateEnded;

	private int turn;

	@OneToOne
	private Player winner;

	@Lob
	@Convert(converter = PionArrayConverter.class)
	private Pion[][] board;

}
