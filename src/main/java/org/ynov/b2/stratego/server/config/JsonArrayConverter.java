/**
 *
 */
package org.ynov.b2.stratego.server.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.gson.Gson;

/**
 * @author sebboursier
 *
 */
@Converter(autoApply = true)
public class JsonArrayConverter implements AttributeConverter<Object[][], String> {

	private final static Gson GSON = new Gson();

	@Override
	public String convertToDatabaseColumn(Object[][] arg0) {
		return GSON.toJson(arg0);
	}

	@Override
	public Object[][] convertToEntityAttribute(String arg0) {
		return GSON.fromJson(arg0, Object[][].class);
	}

}
