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
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.socket.messager.GameMessenger;
import org.ynov.b2.stratego.server.socket.messager.LobbyMessager;
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
	private LobbyMessager lobbyMessager;

	@Autowired
	private GameMessenger gameMessenger;

	@Autowired
	private GameRepository gameRepository;

	@RequestMapping("newGame")
	public HttpEntity<?> newGame() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(-1, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(-2, pionsTwo);

		gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 3, Direction.HAUT, 1));
		gameMessenger.play(startGames[1].getIdPlayer(), new Move(0, 6, Direction.BAS, 1));

		final Game game = gameRepository.getOne(startGames[0].getIdGame());
		game.setDateEnded(new Date());
		gameRepository.save(game);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping("sandBox")
	public HttpEntity<?> sandBox() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
