package app.m2i.medic.initdb.elastic;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.logs.factories.LoggerFactory;
import app.m2i.medic.logs.loggers.Logger;
import app.m2i.medic.recherche.implementations.MedicamentGouvServiceImpl;
import app.m2i.medic.recherche.models.elastic.MedicamentGouvElastic;
import app.m2i.medic.recherche.models.mongo.MedicamentGouvMongo;
import app.m2i.medic.recherche.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.recherche.repositories.mongo.MedicamentGouvRepositoryMongo;

public class InitialisationElasticIndexes {

	private final Logger LOGGER;
	private RestHighLevelClient client;
	ObjectMapper mapper;
	private MedicamentGouvRepositoryMongo mongoRepository;
	private MedicamentGouvRepositoryElastic elasticRepository;

	public InitialisationElasticIndexes(RestHighLevelClient client, ObjectMapper mapper,
			MedicamentGouvRepositoryMongo mongoRepository, MedicamentGouvRepositoryElastic elasticRepository, LoggerFactory factory) {
		super();
		this.client = client;
		this.mapper = mapper;
		this.mongoRepository = mongoRepository;
		this.elasticRepository = elasticRepository;
		LOGGER = factory.getElasticLogger(MedicamentGouvServiceImpl.class.getName());

	}

	public void initElasticIndex() throws IOException {
		if (checkIfIndexExists()) {
			LOGGER.info("Initialisation de la base de donnee elasticsearch");
			List<MedicamentGouvMongo> liste = mongoRepository.findAll();
			System.out.println(liste.get(1560));
			for (int i=0; i<500; i++) {
				System.out.println(liste.get(i).getDenomination());
				MedicamentGouvElastic medicamentGouvElastic = new MedicamentGouvElastic(liste.get(i));
				elasticRepository.save(medicamentGouvElastic);
			}
//			for (MedicamentGouvMongo medicamentGouvMongo : liste) {
//				System.out.println(medicamentGouvMongo.getDenomination());
//				MedicamentGouvElastic medicamentGouvElastic = new MedicamentGouvElastic(medicamentGouvMongo);
//				elasticRepository.save(medicamentGouvElastic);
//			}
		}
	}

	private Boolean checkIfIndexExists() throws IOException {

		var resp = client.getLowLevelClient().performRequest(new Request("GET", "medicgouv/_stats"));
		var body = mapper.readTree(resp.getEntity().getContent());
		var size = body.get("indices").get("medicgouv").get("primaries").get("store").get("size_in_bytes");

		return size.asInt() <= 208;
	}

}
