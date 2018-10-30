/**
 *
 */
package org.ynov.b2.stratego.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.socket.messenger.GameMessenger;
import org.ynov.b2.stratego.server.socket.model.StartGame;
import org.ynov.b2.stratego.server.socket.model.Turn;

/**
 * @author sebboursier
 *
 */
@Service
public class IaService {

	private Map<Integer, Integer> games = new HashMap<>();

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameMessenger gameMessenger;

	@Autowired
	private BouchonService bouchonService;

	public void play(final Integer idGame, final Turn turn) {
		if (games.containsKey(idGame)) {
			if (turn.getResult().equals(MoveResult.DEFEAT) || turn.getResult().equals(MoveResult.VICTORY)) {
				games.remove(idGame);
			} else {
				final Player player = playerRepository.getOne(games.get(idGame));
				if (turn.getTurn() % 2 != player.getNum()) {
					gameMessenger.play(player.getId(), bouchonService.generateMove());
				}
			}
		}
	}

	public void start(final StartGame startGame) {
		games.put(startGame.getIdGame(), startGame.getIdPlayer());
		if (startGame.getNum() == 0) {
			final Turn turn = new Turn();
			turn.setTurn(-1);
			play(startGame.getIdGame(), turn);
		}
	}

}
