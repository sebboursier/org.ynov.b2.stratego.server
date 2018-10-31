/**
 *
 */
package org.ynov.b2.stratego.server.socket.model;

import java.io.Serializable;

import org.ynov.b2.stratego.server.jpa.model.Direction;

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
public class ReceiveTurn implements Serializable {

	private static final long serialVersionUID = 1L;

	private int x;

	private int y;

	private Direction direction;

	private int nb;

	private String uuid;

	public ReceiveTurn(int x, int y, Direction direction, int nb, String uuid) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.nb = nb;
		this.uuid = uuid;
	}

}
