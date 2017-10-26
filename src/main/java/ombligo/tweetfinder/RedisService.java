package ombligo.tweetfinder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
class RedisService {

    private final JedisPool jedisPool;

    public RedisService(@Value("${redis.host}") String host,
                        @Value("${redis.port}") int port,
                        @Value("${redis.ssl}") boolean ssl) {
        jedisPool = new JedisPool(new JedisPoolConfig(), host, port, ssl);
    }

    String get(String key) {
        log.trace("get(): key={}", key);
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    void addToSet(String key, Collection<String> members) {
        if (members.isEmpty()) {
            log.info("collection is empty. cache is not changed");
            return;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            // Wrapping multiple methods in a transaction ensures the operation is atomic
            jedis.sadd(key, members.toArray(new String[0]));
        }
    }

}