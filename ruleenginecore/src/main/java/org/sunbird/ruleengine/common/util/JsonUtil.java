package org.sunbird.ruleengine.common.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	private static final Logger logger = LogManager.getLogger(JsonUtil.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	public static Object parse(String json) {
		try {
			return objectMapper.readValue(json, Object.class);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return null;
	}

	public static String toString(Object json) {
		try {
			return objectMapper.writeValueAsString(json);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return null;
	}

}
