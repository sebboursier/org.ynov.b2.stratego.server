/**
 *
 */
package org.ynov.b2.stratego.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.ynov.b2.stratego.server.jpa.model.PionType;

/**
 * @author sebboursier
 *
 */
@Service
public class BouchonService {

	public PionType[][] generateStarter() {
		List<PionType> types = new ArrayList<>();
		for (PionType pionType : PionType.values()) {
			for (int i = 0; i < pionType.getNb(); i++) {
				types.add(pionType);
			}
		}
		Collections.shuffle(types);

		PionType[][] pions = new PionType[10][4];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				pions[x][y] = types.get(x + y * 10);
			}
		}

		return pions;
	}

}
