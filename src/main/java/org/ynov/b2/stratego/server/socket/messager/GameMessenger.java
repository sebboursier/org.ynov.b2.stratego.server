/**
 *
 */
package org.ynov.b2.stratego.server.socket.messager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.model.Turn;
import org.ynov.b2.stratego.server.util.exception.NotMyTurnException;
import org.ynov.b2.stratego.server.util.exception.TurnException;

/**
 * @author sebboursier
 *
 */
@Controller
public class GameMessenger {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private MoveRepository moveRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private StrategoService strategoService;

	@MessageMapping("/game/{idPlayer}")
	public Move play(@DestinationVariable Integer idPlayer, Move move) {
		final Player player = playerRepository.getOne(idPlayer);
		move.setTurn(player.getGame().getTurn());
		move.setPlayer(player);
		move.setGame(player.getGame());

		try {
			strategoService.proceedTurn(move);
		} catch (NotMyTurnException e) {
			return null;
		} catch (TurnException e) {
			move.setResult(MoveResult.FAIL);
		}

		moveRepository.save(move);
		final Game game = player.getGame();
		simpMessagingTemplate.convertAndSend("/listen/game/" + player.getGame().getId(), new Turn(move));
		game.setTurn(game.getTurn() + 1);
		gameRepository.save(game);
		return moveRepository.save(move);
	}

}
