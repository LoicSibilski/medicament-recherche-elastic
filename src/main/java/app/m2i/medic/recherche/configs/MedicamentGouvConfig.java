package app.m2i.medic.recherche.configs;

import java.io.IOException;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.logs.factories.LoggerFactory;
import app.m2i.medic.recherche.implementations.MedicamentGouvServiceImpl;
import app.m2i.medic.recherche.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.recherche.repositories.mongo.MedicamentGouvRepositoryMongo;

@Configuration
public class MedicamentGouvConfig {

	@Bean
	public MedicamentGouvServiceImpl meidcGouvServiceFactory(ObjectMapper mapper, RestHighLevelClient client,
			LoggerFactory factory, MedicamentGouvRepositoryMongo mongoRepository, MedicamentGouvRepositoryElastic elasticRepository) throws IllegalArgumentException, IOException {
		return new MedicamentGouvServiceImpl(mapper, client, factory, mongoRepository, elasticRepository);
	}

}
