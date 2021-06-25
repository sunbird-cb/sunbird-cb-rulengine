package org.sunbird.ruleengine.common.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CommonMessageSerializer extends JsonSerializer<CommonMessages> {

	@Override
	public void serialize(CommonMessages value, JsonGenerator generator, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		
		// output the custom Json
        generator.writeStartObject();
       
        // the type
        generator.writeFieldName("code");
        generator.writeString(value.getCode());
       
        // the full name
        generator.writeFieldName("message");
        generator.writeString(value.getMessage());
       
        // the full name
        generator.writeFieldName("name");
        generator.writeString(value.toString());
       
        // end tag
        generator.writeEndObject();
		
	}

}
