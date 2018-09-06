/**
 *
 */
package org.ynov.b2.stratego.server.model;

import lombok.Getter;

/**
 * @author sebboursier
 *
 */
@Getter
public enum Pions {

	MARECHAL(10, 1),

	GENERAL(9, 1),

	COLONEL(8, 2),

	COMMANDANT(7, 3),

	CAPITAINE(6, 4),

	LIEUTENANT(5, 4),

	SERGENT(4, 4),

	DEMINEUR(3, 5),

	ECLAIREUR(2, 8),

	ESPION(1, 1),

	FLAG(0, 1),

	BOMBE(0, 6),

	IMPASSABLE(0, 0);

	private final int force;

	private final int nb;

	private Pions(final int force, final int nb) {
		this.force = force;
		this.nb = nb;
	}

}
