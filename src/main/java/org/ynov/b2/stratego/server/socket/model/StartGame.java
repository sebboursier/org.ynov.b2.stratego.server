/**
 *
 */
package org.ynov.b2.stratego.server.socket.model;

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
public class StartGame {

	private Integer idPlayer;

	private int num;

	public StartGame(final Integer idPlayer, final int num) {
		this.idPlayer = idPlayer;
		this.num = num;
	}

}
