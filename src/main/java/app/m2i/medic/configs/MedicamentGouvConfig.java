package app.m2i.medic.configs;

import java.io.IOException;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.implementations.MedicamentGouvServiceImpl;
import app.m2i.medic.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.repositories.mongo.MedicamentGouvRepositoryMongo;

@Configuration
public class MedicamentGouvConfig {
	
	@Bean
	public MedicamentGouvServiceImpl meidcGouvServiceFactory(ObjectMapper mapper,
			 MedicamentGouvRepositoryMongo mongoRepository,MedicamentGouvRepositoryElastic elasticRepository, RestHighLevelClient client) throws IllegalArgumentException, IOException {
		return new MedicamentGouvServiceImpl(mapper, mongoRepository, elasticRepository, client);
	}


}
