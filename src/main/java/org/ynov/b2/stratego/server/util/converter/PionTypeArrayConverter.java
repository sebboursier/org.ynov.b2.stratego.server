/**
 *
 */
package org.ynov.b2.stratego.server.util.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.ynov.b2.stratego.server.jpa.model.PionType;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@Converter(autoApply = true)
public class PionTypeArrayConverter implements AttributeConverter<PionType[][], String> {

	private final static Gson GSON = new Gson();

	@Override
	public String convertToDatabaseColumn(PionType[][] arg0) {
		return GSON.toJson(arg0);
	}

	@Override
	public PionType[][] convertToEntityAttribute(String arg0) {
		return GSON.fromJson(arg0, PionType[][].class);
	}

}
