/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.ynov.b2.stratego.server.socket.model.ReceiveTurn;

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
public class Move extends SuperEntity {

	private static final long serialVersionUID = 1L;

	private int turn;

	@Enumerated(EnumType.STRING)
	private MoveResult result;

	@Enumerated(EnumType.STRING)
	private FightResult fight;

	@ManyToOne
	private Player player;

	@ManyToOne
	private Game game;

	private int x;

	private int y;

	private Direction direction;

	private int nb;

	private Date date;

	public Move(final ReceiveTurn receiveTurn, final Game game, final Player player) {
		this.x = receiveTurn.getX();
		this.y = receiveTurn.getY();
		this.direction = receiveTurn.getDirection();
		this.nb = receiveTurn.getNb();
		this.game = game;
		this.player = player;
		this.turn = game.getTurn();
		date = new Date();
	}

}
