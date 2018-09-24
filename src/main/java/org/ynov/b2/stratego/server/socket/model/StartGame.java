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

	private Integer idGame;

	private Integer idPlayer;

	private int num;

	public StartGame(final Integer idGame, final Integer idPlayer, final int num) {
		this.idGame = idGame;
		this.idPlayer = idPlayer;
		this.num = num;
	}

}
