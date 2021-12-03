package io.fabric8.quickstarts.camel.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMqConfiguration {

    @Value("${amq.url}")
    String brokerURL;

    @Value("${amq.username}")
    String userName;

    @Value("${amq.password}")
    String password;

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqConfiguration.class.getName());


    @Bean("activemq")
    public ActiveMQComponent activeMq(PooledConnectionFactory pooledConnectionFactory) {
        ActiveMQComponent activeMqComponent = new ActiveMQComponent();
        activeMqComponent.setConnectionFactory(pooledConnectionFactory);
        activeMqComponent.setCacheLevelName("CACHE_CONSUMER");


        return activeMqComponent;
    }

    @Bean
    public PooledConnectionFactory pooledConnectionFactory() {
        return new PooledConnectionFactory(connectionFactory());
    }

    private ActiveMQConnectionFactory connectionFactory() {

        logger.info("Configuring connection factory with brokerUrl {}, username {} and password ****", brokerURL, userName);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerURL);
        connectionFactory.setUserName(userName);
        connectionFactory.setPassword(password);

        return connectionFactory;
    }

}