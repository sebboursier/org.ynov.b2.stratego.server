/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import java.io.Serializable;

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
public class Pion implements Serializable {

	private static final long serialVersionUID = 1L;

	private PionType type;

	private Integer num;

	private boolean revelated;

	public Pion(final Integer num, final PionType type) {
		this.num = num;
		this.type = type;
		revelated = false;
	}
}
