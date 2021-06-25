package org.sunbird.ruleengine.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("{clientCode}/public")
public class HealthCheckRest {

	@RequestMapping(value = "/metrics", method = RequestMethod.GET)
	public @ResponseBody void metrics(@PathVariable("clientCode") String clientCode) {

		return;
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public @ResponseBody String ping(@PathVariable("clientCode") String clientCode) {

		return "OIP SERVER REACHED SUCCESSFULLY";
	}

}
