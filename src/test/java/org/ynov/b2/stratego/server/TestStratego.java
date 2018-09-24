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
import org.ynov.b2.stratego.server.jpa.model.Direction;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;
import org.ynov.b2.stratego.server.redis.repository.TeamInLobbyRepository;
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.messager.GameMessenger;
import org.ynov.b2.stratego.server.socket.messager.LobbyMessager;
import org.ynov.b2.stratego.server.socket.model.StartGame;
import org.ynov.b2.stratego.server.util.FightResult;

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
	private GameMessenger gameMessenger;

	@Autowired
	private StrategoService strategoService;

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
	public void testFight() {
		FightResult result = null;

		result = strategoService.proceedFight(PionType.ESPION, PionType.MARECHAL);
		Assert.assertEquals(FightResult.WIN, result);

		result = strategoService.proceedFight(PionType.MARECHAL, PionType.ESPION);
		Assert.assertEquals(FightResult.WIN, result);

		result = strategoService.proceedFight(PionType.DEMINEUR, PionType.BOMBE);
		Assert.assertEquals(FightResult.WIN, result);

		result = strategoService.proceedFight(PionType.LIEUTENANT, PionType.BOMBE);
		Assert.assertEquals(FightResult.LOOSE, result);

		result = strategoService.proceedFight(PionType.SERGENT, PionType.GENERAL);
		Assert.assertEquals(FightResult.LOOSE, result);

		result = strategoService.proceedFight(PionType.COLONEL, PionType.COMMANDANT);
		Assert.assertEquals(FightResult.WIN, result);

		result = strategoService.proceedFight(PionType.CAPITAINE, PionType.CAPITAINE);
		Assert.assertEquals(FightResult.BOTH, result);

		result = strategoService.proceedFight(PionType.LIEUTENANT, PionType.FLAG);
		Assert.assertEquals(FightResult.VICTORY, result);
	}

	@Test
	@Transactional
	public void testLobby() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		pionsOne[1][1] = PionType.FLAG;
		lobbyMessager.enter(idOne, pionsOne);

		final PionType[][] pionsTwo = bouchonService.generateStarter();
		pionsTwo[1][1] = PionType.BOMBE;
		final Integer gameId = lobbyMessager.enter(idTwo, pionsTwo)[0].getIdGame();

		final Game game = gameRepository.getOne(gameId);

		Assert.assertNotNull(game);

		Assert.assertEquals(PionType.FLAG, game.getBoard()[1][1].getType());
		Assert.assertEquals(PionType.BOMBE, game.getBoard()[8][8].getType());
	}

	@Test
	@Transactional
	public void testNotMyTurn() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(idOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(idTwo, pionsTwo);

		Move result = gameMessenger.play(startGames[1].getIdPlayer(), new Move(0, 0, Direction.HAUT, 1));

		Assert.assertNull(result);
	}

	@Test
	@Transactional
	public void testPlayNotValid() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(idOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(idTwo, pionsTwo);

		Move result = gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 0, Direction.HAUT, 1));

		Assert.assertNotNull(result);
		Assert.assertFalse(result.isValid());
	}

	@Test
	@Transactional
	public void testPlayValid() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(idOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(idTwo, pionsTwo);

		Move result = gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 3, Direction.HAUT, 1));

		Assert.assertNotNull(result);
		Assert.assertTrue(result.isValid());
	}

}
