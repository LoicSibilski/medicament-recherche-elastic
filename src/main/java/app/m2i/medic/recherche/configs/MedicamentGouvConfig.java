package app.m2i.medic.recherche.configs;

import java.io.IOException;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.logs.factories.LoggerFactory;
import app.m2i.medic.recherche.implementations.MedicamentGouvServiceImpl;

@Configuration
public class MedicamentGouvConfig {

	@Bean
	public MedicamentGouvServiceImpl meidcGouvServiceFactory(ObjectMapper mapper, RestHighLevelClient client,
			LoggerFactory factory) throws IllegalArgumentException, IOException {
		return new MedicamentGouvServiceImpl(mapper, client, factory);
	}

}
