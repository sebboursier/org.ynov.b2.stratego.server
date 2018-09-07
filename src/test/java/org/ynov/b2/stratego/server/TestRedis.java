/**
 *
 */
package org.ynov.b2.stratego.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.redis.model.TeamInLobby;
import org.ynov.b2.stratego.server.redis.repository.TeamInLobbyRepository;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRedis {

	@Autowired
	private TeamInLobbyRepository teamInLobbyRepository;

	@Before
	public void before() {
		teamInLobbyRepository.deleteAll();
	}

	@Test
	public void testRedisBase() {
		TeamInLobby bean = new TeamInLobby();
		bean.setId(1);
		PionType[][] pions = new PionType[][] { { PionType.FLAG, PionType.BOMBE }, { PionType.BOMBE, PionType.FLAG } };
		bean.setStarter(new Gson().toJson(pions));
		teamInLobbyRepository.save(bean);

		bean = teamInLobbyRepository.findById(1).get();
		pions = new Gson().fromJson(bean.getStarter(), PionType[][].class);
		Assert.assertEquals(PionType.FLAG, pions[0][0]);
		Assert.assertEquals(PionType.BOMBE, pions[0][1]);
	}

}
