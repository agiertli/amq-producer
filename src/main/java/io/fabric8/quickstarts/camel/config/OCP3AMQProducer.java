package io.fabric8.quickstarts.camel.config;

import com.github.javafaker.Faker;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OCP3AMQProducer extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:foo?fixedRate=true&period=5000").bean(this, "generateFakePerson()").to("log:info")
               .to("activemq:queue:test");

    }

    public String generateFakePerson() {
        Faker faker = new Faker();

        return faker.name().fullName() + " lives on " + faker.address().streetAddress();
    }

}
