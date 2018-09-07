/**
 *
 */
package org.ynov.b2.stratego.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.Pion;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;

/**
 * @author sebboursier
 *
 */
@Service
public class StrategoService {

	@Autowired
	private GameRepository gameRepository;

	public boolean proceedTurn(Move move) {
		final Integer numPlayer = move.getPlayer().getNum();
		final Integer turn = move.getTurn();
		final Integer nbPlayers = move.getPlayer().getGame().getPlayers().size();
		if (turn % nbPlayers != numPlayer) {
			return false;
		}
		return true;
	}

	public void startGame(final Integer id) {
		final Game game = gameRepository.getOne(id);
		final Pion[][] pions = new Pion[10][10];
		game.setBoard(pions);

		for (Player player : game.getPlayers()) {
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 4; y++) {
					final Pion pion = new Pion(player.getNum(), player.getPions()[x][y]);
					switch (player.getNum()) {
					case 1:
						pions[9 - x][9 - y] = pion;
						break;
					default:
						pions[x][y] = pion;
						break;
					}
				}
			}
		}

		gameRepository.save(game);
	}

}
