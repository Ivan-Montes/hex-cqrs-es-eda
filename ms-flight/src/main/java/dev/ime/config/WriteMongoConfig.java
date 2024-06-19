package dev.ime.config;

import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "dev.ime.infrastructure.repository.write",
mongoTemplateRef = "writeMongoTemplate")
public class WriteMongoConfig {
	
	@Bean(name = "writeMongoTemplate")
	MongoTemplate writeMongoTemplate(ConfigProperties configProperties) {
		
		ConnectionString connectionString = new ConnectionString(configProperties.getWrite());
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
			      .uuidRepresentation(UuidRepresentation.STANDARD)
			      .applyConnectionString(connectionString).build();
		
		return new MongoTemplate(MongoClients.create(mongoClientSettings), 
						"mongo-write-db");
		
	}
	
}
