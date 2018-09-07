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
import org.ynov.b2.stratego.server.config.JpaConfig;
import org.ynov.b2.stratego.server.model.Game;
import org.ynov.b2.stratego.server.repository.GameRepository;
import org.ynov.b2.stratego.server.repository.PlayerRepository;

/**
 * @author sebboursier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaConfig.class)
public class TestJpa {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameRepository gameRepository;

	@Before
	public void before() {
		playerRepository.deleteAll();
	}

	@Test
	@Transactional
	public void testConvertJsonArray() {
		// Player bean = new Player();
		// bean.setPions(new PionType[][] { { PionType.FLAG, PionType.BOMBE
		// }, { PionType.BOMBE, PionType.FLAG } });
		// bean = playerRepository.save(bean);
		//
		// bean = playerRepository.getOne(bean.getId());
		// Assert.assertEquals(PionType.FLAG, bean.getPions()[0][0]);
		// Assert.assertEquals(PionType.BOMBE, bean.getPions()[0][1]);
	}

	@Test
	@Transactional
	public void testJpaBasique() {
		Game bean = new Game();
		bean = gameRepository.save(bean);

		Assert.assertNotNull(bean.getId());
	}

}
