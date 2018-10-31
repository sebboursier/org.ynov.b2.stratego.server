/**
 *
 */
package org.ynov.b2.stratego.server.socket.messenger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.model.ReceiveTurn;
import org.ynov.b2.stratego.server.socket.model.ResultTurn;
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

	@Transactional
	@MessageMapping("/game/{idGame}")
	public ResultTurn play(final @DestinationVariable Integer idGame, final ReceiveTurn receiveTurn) {
		Game game = gameRepository.getOne(idGame);
		final Player player = playerRepository.findByGameIdAndTeamUuid(idGame, receiveTurn.getUuid());

		System.out.println("##### " + player.getNum() + " / " + (game.getTurn() + 1));

		if (game == null || player == null || game.getDateEnded() != null
				|| Math.abs(game.getTurn() % 2) == player.getNum()) {
			return null;
		}

		game.setTurn(game.getTurn() + 1);
		Move move = new Move(receiveTurn, game, player);

		try {
			strategoService.proceedTurn(move);
		} catch (TurnException e) {
			strategoService.proceedFail(move);
		}

		strategoService.watchEnd(game, move);
		game = gameRepository.save(game);
		move.setGame(game);
		move = moveRepository.save(move);

		final ResultTurn resultTurn = new ResultTurn(move);
		System.out.println(new Gson().toJson(resultTurn));

		simpMessagingTemplate.convertAndSend("/listen/game/" + idGame, resultTurn);
		return resultTurn;
	}

}
