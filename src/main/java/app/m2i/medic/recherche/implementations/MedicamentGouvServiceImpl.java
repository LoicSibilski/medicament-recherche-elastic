package app.m2i.medic.recherche.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.logs.factories.LoggerFactory;
import app.m2i.medic.logs.loggers.Logger;
import app.m2i.medic.recherche.models.elastic.MedicamentGouvElastic;
import app.m2i.medic.recherche.models.mongo.MedicamentGouvMongo;
import app.m2i.medic.recherche.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.recherche.repositories.mongo.MedicamentGouvRepositoryMongo;
import app.m2i.medic.recherche.services.MedicamentGouvService;

public class MedicamentGouvServiceImpl implements MedicamentGouvService {

	private final Logger LOGGER;
	private RestHighLevelClient client;
	ObjectMapper mapper;
	MedicamentGouvRepositoryMongo mongoRepository;
	MedicamentGouvRepositoryElastic elasticRepository;

	public MedicamentGouvServiceImpl(ObjectMapper mapper, RestHighLevelClient client, LoggerFactory factory,
			MedicamentGouvRepositoryMongo mongoRepository, MedicamentGouvRepositoryElastic elasticRepository)
			throws IllegalArgumentException, IOException {
		this.mapper = mapper;
		this.client = client;
		LOGGER = factory.getElasticLogger(MedicamentGouvServiceImpl.class.getName());
		this.elasticRepository = elasticRepository;
		this.mongoRepository = mongoRepository;
	}

	@Override
	public List<String> findByDenomination(String denomination) {

		SearchRequest searchRequest = new SearchRequest("medicgouv");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		CompletionSuggestionBuilder termSuggestionBuilder = SuggestBuilders.completionSuggestion("denomination")
				.prefix(denomination);
		SuggestBuilder suggestBuilder = new SuggestBuilder();
		suggestBuilder.addSuggestion("suggestio-denomi", termSuggestionBuilder);
		searchSourceBuilder.suggest(suggestBuilder);

		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;

		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOGGER.warn("Echec dans la requete de recherche de denomination par suggestion");
			e.printStackTrace();
		}

		Suggest suggest = searchResponse.getSuggest();

		CompletionSuggestion entries = suggest.getSuggestion("suggestio-denomi");

		List<String> listeReturned = new ArrayList<>();

		for (CompletionSuggestion.Entry entry : entries) {
			for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
				listeReturned.add(option.getText().string());
			}
		}
		LOGGER.info("Reussite de la requete avec la suggestion suivante : " + denomination);
		return listeReturned;
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
