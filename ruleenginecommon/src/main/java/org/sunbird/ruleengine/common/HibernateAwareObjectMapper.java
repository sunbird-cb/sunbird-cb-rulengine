package org.sunbird.ruleengine.common;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
@Configuration
public class HibernateAwareObjectMapper extends ObjectMapper {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HibernateAwareObjectMapper() {
		Hibernate5Module module=new Hibernate5Module();
		module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                   false);
		//this.setTimeZone(TimeZone.getTimeZone("utc"));
        registerModule(module);
    }
}
