/**
 *
 */
package org.ynov.b2.stratego.server.socket.messenger;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author sebboursier
 *
 */
@Controller
public class TestMessenger {

	@MessageMapping("/coucou")
	@SendTo("/listen/coucou")
	public String coucou() {
		System.out.println("COUCOUCOUC");
		return "COUCOU";
	}

	@MessageMapping("/test/{uuidTeam}/test")
	public void uuidTeam(@DestinationVariable String uuidTeam, String coucou) {
		System.out.println("/test/{" + uuidTeam + "}/test");
		System.out.println(coucou);
	}

}
