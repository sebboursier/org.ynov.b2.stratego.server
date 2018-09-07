/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import lombok.Getter;

/**
 * @author sebboursier
 *
 */
@Getter
public enum Direction {

	HAUT(0, 1),

	BAS(0, -1),

	DROITE(1, 0),

	GAUCHE(-1, 0);

	private final int x;

	private final int y;

	private Direction(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

}
