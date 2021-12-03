package io.fabric8.quickstarts.camel.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.xnio.Pooled;

import java.util.Properties;

@Configuration
public class ActiveMqConfiguration {

    @Value("${amq.producer.url}")
    String producerBrokerURL;

    @Value("${amq.producer.username}")
    String  producerUserName;

    @Value("${amq.producer.password}")
    String producerPassword;

    @Value("${amq.consumer.url}")
    String consumerBrokerURL;

    @Value("${amq.consumer.username}")
    String consumerUserName;

    @Value("${amq.consumer.password}")
    String consumerPassword;

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqConfiguration.class.getName());


    @Bean("activemq-consumer")
    public ActiveMQComponent activeMqConsumer(@Qualifier("consumerpcf") PooledConnectionFactory consumerPooledConnectionFactory) {
        ActiveMQComponent activeMqComponent = new ActiveMQComponent();
        activeMqComponent.setConnectionFactory(consumerPooledConnectionFactory);
        activeMqComponent.setCacheLevelName("CACHE_CONSUMER");
        return activeMqComponent;
    }

    @Bean("activemq-producer")
    public ActiveMQComponent activeMq(@Qualifier("producerpcf") PooledConnectionFactory producerPooledConnectionFactory) {
        ActiveMQComponent activeMqComponent = new ActiveMQComponent();
        activeMqComponent.setConnectionFactory(producerPooledConnectionFactory);
        activeMqComponent.setCacheLevelName("CACHE_CONSUMER");
        return activeMqComponent;
    }

    @Bean("producerpcf")
    @Primary
    public PooledConnectionFactory producerPooledConnectionFactory() {
        PooledConnectionFactory pcf =  new PooledConnectionFactory(producerConnectionFactory());
        Properties props = new Properties();
        props.setProperty("maxConnections","1");
        pcf.setProperties(props);
        return pcf;
    }

    @Bean("consumerpcf")
    public PooledConnectionFactory consumerPooledConnectionFactory() {
        PooledConnectionFactory pcf =  new PooledConnectionFactory(consumerConnectionFactory());
        Properties props = new Properties();
        props.setProperty("maxConnections","1");
        pcf.setProperties(props);
        return pcf;
    }

    @Bean("producerCF")
    public ActiveMQConnectionFactory producerConnectionFactory() {

        logger.info("Configuring connection factory with brokerUrl {}, username {} and password ****", producerBrokerURL, producerUserName);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(producerBrokerURL);
        connectionFactory.setUserName(producerUserName);
        connectionFactory.setPassword(producerPassword);
        return connectionFactory;
    }


    @Bean("consumerCF")
    public ActiveMQConnectionFactory consumerConnectionFactory() {

        logger.info("Configuring connection factory with brokerUrl {}, username {} and password ****", consumerBrokerURL, consumerUserName);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(consumerBrokerURL);
        connectionFactory.setUserName(consumerUserName);
        connectionFactory.setPassword(consumerPassword);
        return connectionFactory;
    }

}