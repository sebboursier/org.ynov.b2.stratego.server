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
import org.ynov.b2.stratego.server.jpa.model.FightResult;
import org.ynov.b2.stratego.server.jpa.model.Game;
import org.ynov.b2.stratego.server.jpa.model.Move;
import org.ynov.b2.stratego.server.jpa.model.MoveResult;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.model.TeamGroupe;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;
import org.ynov.b2.stratego.server.redis.repository.TeamInLobbyRepository;
import org.ynov.b2.stratego.server.service.BouchonService;
import org.ynov.b2.stratego.server.service.StrategoService;
import org.ynov.b2.stratego.server.socket.messenger.GameMessenger;
import org.ynov.b2.stratego.server.socket.messenger.LobbyMessenger;
import org.ynov.b2.stratego.server.socket.model.StartGame;

/**
 * @author sebboursier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestStratego {

	@Autowired
	private LobbyMessenger lobbyMessager;

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

	private String uuidOne;

	private String uuidTwo;

	@Before
	public void before() {
		teamInLobbyRepository.deleteAll();
		moveRepository.deleteAll();
		playerRepository.deleteAll();
		gameRepository.deleteAll();
		teamRepository.deleteAll();

		uuidOne = teamRepository.save(new Team(-1, TeamGroupe.TEST, "TEST_1")).getUuid();
		uuidTwo = teamRepository.save(new Team(-2, TeamGroupe.TEST, "TEST_2")).getUuid();
	}

	@Test
	@Transactional
	public void testDefeat() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(uuidOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(uuidTwo, pionsTwo);

		Move move = new Move();
		for (int i = 0; i < 9; i++) {
			Assert.assertNotEquals(MoveResult.DEFEAT, move.getResult());
			final Integer idPlayer = startGames[i % 2].getIdPlayer();
			move = gameMessenger.play(idPlayer, new Move(0, 0, Direction.BAS, 1));
		}
		Assert.assertEquals(MoveResult.DEFEAT, move.getResult());
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
		Assert.assertEquals(FightResult.WIN, result);
	}

	@Test
	@Transactional
	public void testLobby() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		pionsOne[1][1] = PionType.FLAG;
		lobbyMessager.enter(uuidOne, pionsOne);

		final PionType[][] pionsTwo = bouchonService.generateStarter();
		pionsTwo[1][1] = PionType.BOMBE;
		final Integer gameId = lobbyMessager.enter(uuidTwo, pionsTwo)[0].getIdGame();

		final Game game = gameRepository.getOne(gameId);

		Assert.assertNotNull(game);

		Assert.assertEquals(PionType.FLAG, game.getBoard()[1][1].getType());
		Assert.assertEquals(PionType.BOMBE, game.getBoard()[8][8].getType());
	}

	@Test
	@Transactional
	public void testNotMyTurn() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(uuidOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(uuidTwo, pionsTwo);

		Move result = gameMessenger.play(startGames[1].getIdPlayer(), new Move(0, 0, Direction.HAUT, 1));

		Assert.assertNull(result);
	}

	@Test
	@Transactional
	public void testPlayNotValid() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(uuidOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(uuidTwo, pionsTwo);

		Move move = gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 0, Direction.HAUT, 1));

		Assert.assertNotNull(move);
		Assert.assertEquals(MoveResult.FAIL, move.getResult());
	}

	@Test
	@Transactional
	public void testPlayValid() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		pionsOne[0][3] = PionType.SERGENT;
		lobbyMessager.enter(uuidOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(uuidTwo, pionsTwo);

		Move move = gameMessenger.play(startGames[0].getIdPlayer(), new Move(0, 3, Direction.HAUT, 1));

		Assert.assertNotNull(move);
		Assert.assertEquals(MoveResult.MOVE, move.getResult());
	}

	@Test
	@Transactional
	public void testTeam() {
		final PionType[][] pionsOne = bouchonService.generateStarter();
		lobbyMessager.enter(uuidOne, pionsOne);
		final PionType[][] pionsTwo = bouchonService.generateStarter();
		final StartGame[] startGames = lobbyMessager.enter(uuidTwo, pionsTwo);

		final Integer idGame = startGames[0].getIdGame();
		final Game game = gameRepository.getOne(idGame);

		Assert.assertEquals(2, game.getPlayers().size());
		for (Player player : game.getPlayers()) {
			Assert.assertNotNull(player.getTeam());
		}
	}

}
