/**
 *
 */
package org.ynov.b2.stratego.server.socket.model;

import org.ynov.b2.stratego.server.jpa.model.Direction;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;

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
public class Turn {

	private Integer idGame;

	private int turn;

	private int x;

	private int y;

	private Direction direction;

	private int nb;

	private MoveResult result;

	public Turn(final Move move) {
		turn = move.getTurn();
		x = move.getX();
		y = move.getY();
		direction = move.getDirection();
		nb = move.getNb();
		result = move.getResult();
		idGame = move.getGame().getId();
	}

}
