/**
 *
 */
package org.ynov.b2.stratego.server.service;

import org.springframework.stereotype.Service;
import org.ynov.b2.stratego.server.jpa.model.PionType;

/**
 * @author sebboursier
 *
 */
@Service
public class BouchonService {

	public PionType[][] generateStarter() {
		PionType[][] pions = new PionType[10][4];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				pions[x][y] = PionType.SERGENT;
			}
		}
		return pions;
	}

}
