package app.m2i.medic.configs;

import org.elasticsearch.client.Client;
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
			 MedicamentGouvRepositoryMongo mongoRepository,MedicamentGouvRepositoryElastic elasticRepository, RestHighLevelClient client) {
		return new MedicamentGouvServiceImpl(mapper, mongoRepository, elasticRepository, client);
	}


}
