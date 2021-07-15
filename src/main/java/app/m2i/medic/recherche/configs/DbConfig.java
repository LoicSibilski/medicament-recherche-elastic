package app.m2i.medic.recherche.configs;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.initdb.elastic.InitialisationElasticIndexes;
import app.m2i.medic.recherche.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.recherche.repositories.mongo.MedicamentGouvRepositoryMongo;

@Configuration
public class DbConfig {
	@Bean
	public InitialisationElasticIndexes initElastic(RestHighLevelClient client, ObjectMapper mapper,
			MedicamentGouvRepositoryMongo mongoRepository, MedicamentGouvRepositoryElastic elasticRepository) {
		return new InitialisationElasticIndexes(client, mapper, mongoRepository, elasticRepository);
	}

}
