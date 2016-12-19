package com.comsysto.metagrapher.ui.application;

import com.comsysto.metagrapher.ui.service.MetagrapherService;
import com.comsysto.metagrapher.ui.eureka.EurekaMetagrapherClientInfoProvider;
import com.comsysto.metagrapher.ui.impl.MetagrapherUiConfigRepository;
import com.comsysto.metagrapher.ui.impl.RedisMetagrapherUiConfigRepository;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfoProvider;
import com.comsysto.metagrapher.ui.web.MetagrapherRestController;
import com.comsysto.metagrapher.ui.web.MetagrapherWebController;
import com.netflix.discovery.EurekaClient;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.charset.StandardCharsets;

@SpringBootApplication

@EnableEurekaClient
@EnableScheduling
public class Application {

    private String clientInfoRedisKey = "metagrapher:client-info:hash";
    private String uiConfigRedisKey = "metagrapher:ui-config";

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

    @Bean
    public MetagrapherWebController metagrapherWebController(){
        return new MetagrapherWebController();
    }

    @Bean
    public MetagrapherRestController metagrapherRestController(){
        return new MetagrapherRestController(metagrapherService(), metagrapherUiConfigRepository());
    }

    @Bean
    public MetagrapherUiConfigRepository metagrapherUiConfigRepository() {
        return new RedisMetagrapherUiConfigRepository(redisTemplate(), uiConfigRedisKey);
    }


    @Bean
    public MetagrapherClientInfoProvider eurekaClientInfoProvider() {
        return new EurekaMetagrapherClientInfoProvider(eurekaClient, "default");
    }


    @Bean
    @Scope("prototype")
    public<T> RedisTemplate<String, T> redisTemplate() {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setDefaultSerializer(xstreamSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public <T> RedisSerializer<T> xstreamSerializer() {
        XStream xstream = xStream();
        return new RedisSerializer<T>() {
            @Override
            public byte[] serialize(T t) throws SerializationException {
                if(t == null){
                    return null;
                }
                return xstream.toXML(t).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            @SuppressWarnings("unchecked")
            public T deserialize(byte[] bytes) throws SerializationException {
                if(bytes == null){
                    return null;
                }
                return (T) xstream.fromXML(new String(bytes, StandardCharsets.UTF_8));
            }
        };
    }

    @Bean
    public XStream xStream() {
        return new XStream();
    }

    @Bean
    public MetagrapherService metagrapherService(){
        return new MetagrapherService(eurekaClientInfoProvider());
    }

}
