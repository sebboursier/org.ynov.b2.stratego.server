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
import org.ynov.b2.stratego.server.model.Pions;
import org.ynov.b2.stratego.server.model.Player;
import org.ynov.b2.stratego.server.repository.PlayerRepository;

/**
 * @author sebboursier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestJpa {

	@Autowired
	private PlayerRepository playerRepository;

	@Before
	public void before() {
		playerRepository.deleteAll();
	}

	@Test
	@Transactional
	public void testConvertJsonArray() {
		Player bean = new Player();
		bean.setPions(new Pions[][] { { Pions.FLAG, Pions.BOMBE }, { Pions.BOMBE, Pions.FLAG } });
		bean = playerRepository.save(bean);

		bean = playerRepository.getOne(bean.getId());
		Assert.assertEquals(Pions.FLAG, bean.getPions()[0][0]);
		Assert.assertEquals(Pions.BOMBE, bean.getPions()[0][1]);
	}

}
