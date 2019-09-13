package com.bridgelabz.fundonotes.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	public ModelMapper getModelMapping() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMethodAccessLevel(AccessLevel.PROTECTED);
		return modelMapper;
	}
	/*
	 * @Bean JedisConnectionFactory jedisConnectionFactory() {
	 * RedisStandaloneConfiguration redisStandaloneConfiguration = new
	 * RedisAutoConfiguration("localhost", 6379);
	 * redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));
	 * return new JedisConnectionFactory(redisStandaloneConfiguration); }
	 * 
	 * @Bean public RedisTemplate redisTemplate() { RedisTemplate template = new
	 * RedisTemplate<>(); template.setConnectionFactory(jedisConnectionFactory());
	 * return template; }
	 */
}
