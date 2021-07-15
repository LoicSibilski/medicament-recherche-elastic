package app.m2i.medic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "app.m2i.medic.repositories.elastic")
public class MedicamentRechercheApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicamentRechercheApplication.class, args);
	}

}
