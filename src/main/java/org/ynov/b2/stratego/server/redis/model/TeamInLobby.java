/**
 *
 */
package org.ynov.b2.stratego.server.redis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sebboursier
 *
 */
@Getter
@Setter
@NoArgsConstructor
@RedisHash(timeToLive = 3600)
public class TeamInLobby {

	@Id
	private Integer id;

	private Long dateCreated;

	private String starter;

}
