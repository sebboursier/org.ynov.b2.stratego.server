/**
 *
 */
package org.ynov.b2.stratego.server.socket.messager;

import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;
import org.ynov.b2.stratego.server.redis.model.TeamInLobby;
import org.ynov.b2.stratego.server.redis.repository.TeamInLobbyRepository;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.model.StartGame;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@Controller
public class LobbyMessager {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TeamInLobbyRepository teamInLobbyRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private StrategoService strategoService;

	@MessageMapping("/lobby/{idTeam}")
	public Integer enter(@DestinationVariable Integer idTeam, PionType[][] pions) {
		TeamInLobby teamInLobby = teamInLobbyRepository.getFirst();

		if (teamInLobby == null) {
			teamInLobby = new TeamInLobby();
			teamInLobby.setId(idTeam);
			teamInLobby.setDateCreated(new Date().getTime());
			teamInLobby.setStarter(new Gson().toJson(pions));
			teamInLobbyRepository.save(teamInLobby);

			return null;
		}

		Game game = new Game();
		game.setTurn(0);
		game.setDateStarted(new Date());
		game.setPlayers(new HashSet<>());

		Player playerOne = new Player();
		playerOne.setNum(0);
		playerOne.setTeam(teamRepository.getOne(teamInLobby.getId()));
		playerOne.setPions(new Gson().fromJson(teamInLobby.getStarter(), PionType[][].class));
		playerOne.setGame(game);
		game.getPlayers().add(playerOne);

		Player playerTwo = new Player();
		playerTwo.setNum(1);
		playerTwo.setTeam(teamRepository.getOne(idTeam));
		playerTwo.setPions(pions);
		playerTwo.setGame(game);
		game.getPlayers().add(playerTwo);

		game = gameRepository.save(game);
		strategoService.startGame(game.getId());

		simpMessagingTemplate.convertAndSend("/listen/lobby/" + teamInLobby.getId(),
				new StartGame(playerOne.getId(), 0));
		simpMessagingTemplate.convertAndSend("/listen/lobby/" + idTeam, new StartGame(playerTwo.getId(), 1));

		teamInLobbyRepository.delete(teamInLobby);

		return game.getId();
	}

}
