/**
 *
 */
package org.ynov.b2.stratego.server.jpa.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
@Entity
public class Move extends SuperEntity {

	private static final long serialVersionUID = 1L;

	private int turn;

	private boolean valid;

	@ManyToOne
	private Player player;

	private int x;

	private int y;

	private Direction direction;

	private int nb;

}
