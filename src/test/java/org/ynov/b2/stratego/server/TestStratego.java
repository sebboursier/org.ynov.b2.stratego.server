/**
 *
 */
package org.ynov.b2.stratego.server;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;
import org.ynov.b2.stratego.server.redis.repository.TeamInLobbyRepository;
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.socket.messager.LobbyMessager;

/**
 * @author sebboursier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestStratego {

	@Autowired
	private LobbyMessager lobbyMessager;

	@Autowired
	private TeamInLobbyRepository teamInLobbyRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MoveRepository moveRepository;

	@Autowired
	private BouchonService bouchonService;

	private Integer idOne;

	private Integer idTwo;

	@Before
	public void before() {
		teamInLobbyRepository.deleteAll();
		playerRepository.deleteAll();
		gameRepository.deleteAll();
		teamRepository.deleteAll();
		moveRepository.deleteAll();

		idOne = teamRepository.save(new Team()).getId();
		idTwo = teamRepository.save(new Team()).getId();
	}

	@Test
	@Transactional
	public void testLobby() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		pionsOne[1][1] = PionType.FLAG;
		lobbyMessager.enter(idOne, pionsOne);

		final PionType[][] pionsTwo = bouchonService.generateStarter();
		pionsTwo[1][1] = PionType.BOMBE;
		final Integer gameId = lobbyMessager.enter(idTwo, pionsTwo);

		final Game game = gameRepository.getOne(gameId);

		Assert.assertNotNull(game);

		Assert.assertEquals(PionType.FLAG, game.getBoard()[1][1].getType());
		Assert.assertEquals(PionType.BOMBE, game.getBoard()[8][8].getType());
	}
}
