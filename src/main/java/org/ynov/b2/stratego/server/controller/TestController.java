/**
 *
 */
package org.ynov.b2.stratego.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ynov.b2.stratego.server.jpa.model.Direction;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.messenger.GameMessenger;
import org.ynov.b2.stratego.server.socket.messenger.LobbyMessenger;
import org.ynov.b2.stratego.server.socket.model.StartGame;

/**
 * @author sebboursier
 *
 */
@RestController
@RequestMapping("test")
public class TestController {

	@Autowired
	private BouchonService bouchonService;

	@Autowired
	private LobbyMessenger lobbyMessager;

	@Autowired
	private GameMessenger gameMessenger;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private StrategoService strategoService;

	@RequestMapping("newGame")
	public HttpEntity<?> newGame() {
		final Team test1 = teamRepository.getOne(-1);
		final Team test2 = teamRepository.getOne(-2);

		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(test1.getUuid(), pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(test2.getUuid(), pionsTwo);

		gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(0, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 4, Direction.HAUT, 1));

		gameMessenger.play(startGames[1].getIdPlayer(), new Move(1, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(1, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(1, 5, Direction.BAS, 1));

		gameMessenger.play(startGames[0].getIdPlayer(), new Move(4, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(4, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(4, 4, Direction.HAUT, 1));

		gameMessenger.play(startGames[1].getIdPlayer(), new Move(5, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(5, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(5, 5, Direction.BAS, 1));

		gameMessenger.play(startGames[0].getIdPlayer(), new Move(8, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(8, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(8, 4, Direction.HAUT, 1));

		gameMessenger.play(startGames[1].getIdPlayer(), new Move(9, 6, Direction.BAS, 1));
		gameMessenger.play(startGames[0].getIdPlayer(), new Move(9, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(9, 5, Direction.BAS, 1));

		final Game game = gameRepository.getOne(startGames[0].getIdGame());
		game.setDateEnded(new Date());
		// for (Pion[] column : game.getBoard()) {
		// for (Pion pion : column) {
		// if (pion != null) {
		// pion.setRevelated(true);
		// }
		// }
		// }
		gameRepository.save(game);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping("randomGame")
	public HttpEntity<?> randomGame() {
		final Team test1 = teamRepository.getOne(-1);
		final Team test2 = teamRepository.getOne(-2);

		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(test1.getUuid(), pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(test2.getUuid(), pionsTwo);

		Move move = new Move();
		int turn = 0;
		while (move == null || move.getResult() == null
				|| (!move.getResult().equals(MoveResult.VICTORY) && !move.getResult().equals(MoveResult.DEFEAT))) {
			final Integer idPlayer = startGames[turn % 2].getIdPlayer();
			move = bouchonService.generateMove();
			move = gameMessenger.play(idPlayer, move);
			turn++;
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping("sandBox")
	public HttpEntity<?> sandBox() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
