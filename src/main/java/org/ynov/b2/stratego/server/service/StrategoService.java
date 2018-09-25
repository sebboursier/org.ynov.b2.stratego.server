/**
 *
 */
package org.ynov.b2.stratego.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.Pion;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.util.FightResult;
import org.ynov.b2.stratego.server.util.exception.NotMyTurnException;
import org.ynov.b2.stratego.server.util.exception.TurnException;

/**
 * @author sebboursier
 *
 */
@Service
public class StrategoService {

	@Autowired
	private GameRepository gameRepository;

	public FightResult proceedFight(final PionType attacker, final PionType defender) {
		if (attacker.equals(defender)) {
			return FightResult.BOTH;
		}
		switch (defender) {
		case FLAG:
			return FightResult.VICTORY;
		case BOMBE:
			if (attacker.equals(PionType.DEMINEUR)) {
				return FightResult.WIN;
			}
			return FightResult.LOOSE;
		case MARECHAL:
			if (attacker.equals(PionType.ESPION)) {
				return FightResult.WIN;
			}
			return FightResult.LOOSE;
		default:
			if (attacker.getForce() > defender.getForce()) {
				return FightResult.WIN;
			}
			return FightResult.LOOSE;
		}
	}

	public void proceedTurn(Move move) throws TurnException {
		final Integer numPlayer = move.getPlayer().getNum();
		final Integer turn = move.getTurn();
		final Integer nbPlayers = move.getPlayer().getGame().getPlayers().size();
		if (turn % nbPlayers != numPlayer) {
			throw new NotMyTurnException();
		}

		final Game game = move.getPlayer().getGame();

		final Pion[][] board = game.getBoard();
		final Pion pion = board[move.getX()][move.getY()];

		if (pion != null && pion.getType().getForce() > 0 && pion.getNum() == numPlayer) {
			if (move.getNb() < 1 || (move.getNb() > 1 && !pion.getType().equals(PionType.ECLAIREUR))) {
				throw new TurnException();
			}

			final int targetX = move.getX() + move.getDirection().getX() * move.getNb();
			final int targetY = move.getY() + move.getDirection().getY() * move.getNb();
			final Pion target = board[targetX][targetY];

			if (target == null) {
				board[targetX][targetY] = pion;
				board[move.getX()][move.getY()] = null;
			} else if (target.getType().equals(PionType.IMPASSABLE)) {
				throw new TurnException();
			} else if (pion.getNum().equals(target.getNum())) {
				throw new TurnException();
			} else {
				pion.setRevelated(true);
				target.setRevelated(true);
				switch (proceedFight(pion.getType(), target.getType())) {
				case WIN:
					board[targetX][targetY] = pion;
					board[move.getX()][move.getY()] = null;
					break;
				case LOOSE:
					board[move.getX()][move.getY()] = null;
					break;
				case BOTH:
					board[targetX][targetY] = null;
					board[move.getX()][move.getY()] = null;
					break;
				case VICTORY:
					// TODO
					break;
				}
			}
		} else {
			throw new TurnException();
		}

		game.setBoard(board);
		gameRepository.save(game);
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
