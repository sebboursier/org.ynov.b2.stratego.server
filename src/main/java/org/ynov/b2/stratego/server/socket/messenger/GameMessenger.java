/**
 *
 */
package org.ynov.b2.stratego.server.socket.messenger;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.service.IaService;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.model.Turn;
import org.ynov.b2.stratego.server.util.exception.NotMyTurnException;
import org.ynov.b2.stratego.server.util.exception.TurnException;

import com.google.gson.Gson;

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

	@Autowired
	private IaService iaService;

	@Transactional
	@MessageMapping("/game/{idPlayer}")
	public Move play(@DestinationVariable Integer idPlayer, Move move) {
		final Player player = playerRepository.getOne(idPlayer);
		if (player.getGame().getDateEnded() != null) {
			return null;
		}

		move.setTurn(player.getGame().getTurn());
		move.setPlayer(player);
		move.setGame(player.getGame());
		move.setDate(new Date());

		try {
			strategoService.proceedTurn(move);
		} catch (NotMyTurnException e) {
			return null;
		} catch (TurnException e) {
			strategoService.proceedFail(move);
		}

		final Game game = player.getGame();
		strategoService.watchEnd(game, move);

		game.setTurn(game.getTurn() + 1);
		gameRepository.save(game);
		move = moveRepository.save(move);
		final Turn turn = new Turn(move);
		System.out.println(new Gson().toJson(turn));

		simpMessagingTemplate.convertAndSend("/listen/game/" + player.getGame().getId(), turn);
		iaService.play(game.getId(), turn);

		return move;
	}

}
