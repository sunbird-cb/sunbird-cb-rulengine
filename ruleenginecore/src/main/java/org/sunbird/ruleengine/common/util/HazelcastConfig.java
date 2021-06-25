package org.sunbird.ruleengine.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;


// @PropertySource("classpath:hazelcast-default.properties")
public class HazelcastConfig {

	@Autowired
	Config config;

	@Value("${hazelcast.spring.cache.prop1}")
	String myValue;

	@Autowired
	HazelcastInstance hazelcastInstance;

	@Bean
	public CacheManager cacheManager() {
		return new HazelcastCacheManager(hazelcastInstance);
	}

	@Bean
	HazelcastInstance hazelCast() {
		return Hazelcast.newHazelcastInstance(config);
	}

	@Bean
	public Config getConfig() {
		Config config = new Config();
		config.setInstanceName("iestate");
		MapConfig allUsersCache = new MapConfig();
		allUsersCache.setTimeToLiveSeconds(30);
		allUsersCache.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("labelCache", allUsersCache);

		MapConfig usercache = new MapConfig();
		usercache.setTimeToLiveSeconds(20);
		usercache.setEvictionPolicy(EvictionPolicy.LFU);
		config.getMapConfigs().put("usercache", usercache);
		return config;
	}
}
