package com.xyz.orderserviceaxon.configuration;

import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.client.MongoClient;

@Configuration
@Profile("mongo")
public class MongoConfiguration {
    
    /**
     * Bean definition for TokenStore using MongoDB.
     * This configuration is active only when the "mongo" profile is active.
     *
     * @param client the MongoClient instance
     * @param serializer the Serializer instance
     * @return the configured TokenStore
     */
    @Bean
    public TokenStore getTokenStore(MongoClient client, Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(DefaultMongoTemplate.builder()
                        .mongoDatabase(client)
                        .build())
                .serializer(serializer)
                .build();
    }   
}
