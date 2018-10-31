/**
 *
 */
package org.ynov.b2.stratego.server.service.ia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.ynov.b2.stratego.server.jpa.model.Direction;
import org.ynov.b2.stratego.server.jpa.model.PionType;
import org.ynov.b2.stratego.server.jpa.model.Player;
import org.ynov.b2.stratego.server.socket.model.ReceiveTurn;

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
public class IaInformations {

	private Player player;

	private Subscription subscription;

	private PionType[][] starter;

	private List<ReceiveTurn> receiveTurnsPossibles;

	public IaInformations(final Player player, final Subscription subscription, final PionType[][] starter) {
		this.player = player;
		this.subscription = subscription;
		this.starter = starter;
		System.out.println(starter.length);
		this.receiveTurnsPossibles = new ArrayList<>();

		addMoveInitial(0, 3);
		addMoveInitial(1, 3);
		addMoveInitial(4, 3);
		addMoveInitial(5, 3);
		addMoveInitial(8, 3);
		addMoveInitial(9, 3);
	}

	private void addMoveInitial(int x, int y) {
		if (starter[x][y].canMove()) {
			boolean playerOne = player.getNum() == 0;
			final Integer bX = playerOne ? x : 9 - x;
			final Integer bY = playerOne ? y : 9 - y;
			final Direction direction = playerOne ? Direction.HAUT : Direction.BAS;
			receiveTurnsPossibles.add(new ReceiveTurn(bX, bY, direction, 1, player.getTeam().getUuid()));
		}
	}

}
