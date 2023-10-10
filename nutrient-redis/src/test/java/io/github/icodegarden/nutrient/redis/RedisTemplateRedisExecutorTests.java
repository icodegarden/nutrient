package io.github.icodegarden.nutrient.redis;

import java.io.Serializable;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutor;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class RedisTemplateRedisExecutorTests extends RedisExecutorTests {

	@Override
	protected RedisExecutor newInstance() {
		return new RedisTemplateRedisExecutor(newRedisTemplate());
	}

	public static RedisTemplate<String, Serializable> newRedisTemplate() {
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
//		redisTemplate.setConnectionFactory(jedisConnectionFactory());//二选一
		redisTemplate.setConnectionFactory(lettuceConnectionFactory());//二选一
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	static RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig)
				.build();

		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName("192.168.184.129");
//		redisConfig.setPassword(RedisPassword.of("8q9P&ZF5SQ@Fv49x"));
		redisConfig.setPort(6379);

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig, clientConfig);
		jedisConnectionFactory.afterPropertiesSet();// 需要调用一下
		return jedisConnectionFactory;
	}
	
	static RedisConnectionFactory lettuceConnectionFactory() {
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName("192.168.184.129");
//		redisConfig.setPassword(RedisPassword.of("8q9P&ZF5SQ@Fv49x"));
		redisConfig.setPort(6379);
		
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig);
		lettuceConnectionFactory.afterPropertiesSet();// 需要调用一下
		return lettuceConnectionFactory;
	}
	
}
