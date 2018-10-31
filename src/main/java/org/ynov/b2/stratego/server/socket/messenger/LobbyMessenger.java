/**
 *
 */
package org.ynov.b2.stratego.server.socket.messenger;

import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

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
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.service.IaService;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.model.StartGame;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@Controller
public class LobbyMessenger {

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

	@Autowired
	private BouchonService bouchonService;

	@Autowired
	private IaService iaService;

	@Transactional
	@MessageMapping("/lobby/{uuidTeam}")
	public StartGame[] enter(@DestinationVariable String uuidTeam, PionType[][] pions) {
		TeamInLobby teamInLobby = teamInLobbyRepository.getFirst();

		if (teamInLobby == null) {
			teamInLobby = new TeamInLobby();
			teamInLobby.setUuid(uuidTeam.toString());
			teamInLobby.setDateCreated(new Date().getTime());
			teamInLobby.setStarter(new Gson().toJson(pions));
			teamInLobbyRepository.save(teamInLobby);

			return null;
		}

		Game game = new Game();
		game.setPlayers(new HashSet<>());

		Player playerOne = new Player();
		playerOne.setNum(0);
		playerOne.setTeam(teamRepository.findByUuid(teamInLobby.getUuid()));
		playerOne.setPions(new Gson().fromJson(teamInLobby.getStarter(), PionType[][].class));
		playerOne.setGame(game);
		game.getPlayers().add(playerOne);

		Player playerTwo = new Player();
		playerTwo.setNum(1);
		playerTwo.setTeam(teamRepository.findByUuid(uuidTeam));
		playerTwo.setPions(pions);
		playerTwo.setGame(game);
		game.getPlayers().add(playerTwo);

		strategoService.startGame(game, playerOne, playerTwo);
		game = gameRepository.save(game);

		final StartGame startOne = new StartGame(game.getId(), playerOne.getTeam().getUuid(), 0);
		simpMessagingTemplate.convertAndSend("/listen/lobby/" + teamInLobby.getUuid(), startOne);

		final StartGame startTwo = new StartGame(game.getId(), playerTwo.getTeam().getUuid(), 1);
		simpMessagingTemplate.convertAndSend("/listen/lobby/" + uuidTeam, startTwo);

		teamInLobbyRepository.delete(teamInLobby);

		return new StartGame[] { startOne, startTwo };
	}

	@Transactional
	@MessageMapping("/lobby/{uuidTeam}/test")
	public StartGame[] enterTest(@DestinationVariable String uuidTeam, PionType[][] pions) {
		try {

			final Game game = new Game();
			gameRepository.saveAndFlush(game);

			Player playerOne = new Player();
			playerOne.setNum(0);
			playerOne.setTeam(teamRepository.findByUuid(uuidTeam));
			playerOne.setPions(pions);
			playerOne.setGame(game);
			playerRepository.saveAndFlush(playerOne);

			Player playerTwo = new Player();
			playerTwo.setNum(1);
			playerTwo.setTeam(teamRepository.getOne(-1));
			playerTwo.setPions(bouchonService.generateStarter());
			playerTwo.setGame(game);
			playerRepository.saveAndFlush(playerTwo);

			strategoService.startGame(game, playerOne, playerTwo);
			gameRepository.saveAndFlush(game);

			Thread.sleep(100);

			final StartGame startOne = new StartGame(game.getId(), playerOne.getTeam().getUuid(), 0);
			simpMessagingTemplate.convertAndSend("/listen/lobby/" + uuidTeam, startOne);

			final StartGame startTwo = new StartGame(game.getId(), playerTwo.getTeam().getUuid(), 1);
			iaService.start(startTwo);

			return new StartGame[] { startOne, startTwo };
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
