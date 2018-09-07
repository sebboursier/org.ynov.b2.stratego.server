/**
 *
 */
package org.ynov.b2.stratego.server.model;

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
public class Pion {

	private PionType type;

	private int num;
}
