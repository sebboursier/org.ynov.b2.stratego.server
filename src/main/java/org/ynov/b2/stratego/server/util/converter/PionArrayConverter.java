/**
 *
 */
package org.ynov.b2.stratego.server.util.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.ynov.b2.stratego.server.jpa.model.Pion;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@Converter(autoApply = true)
public class PionArrayConverter implements AttributeConverter<Pion[][], String> {

	private final static Gson GSON = new Gson();

	@Override
	public String convertToDatabaseColumn(Pion[][] arg0) {
		return GSON.toJson(arg0);
	}

	@Override
	public Pion[][] convertToEntityAttribute(String arg0) {
		return GSON.fromJson(arg0, Pion[][].class);
	}

}
