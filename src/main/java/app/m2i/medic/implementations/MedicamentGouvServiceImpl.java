package app.m2i.medic.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import app.m2i.medic.models.elastic.MedicamentGouvElastic;
import app.m2i.medic.models.mongo.MedicamentGouvMongo;
import app.m2i.medic.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.repositories.mongo.MedicamentGouvRepositoryMongo;
import app.m2i.medic.services.medicamentGouv.MedicamentGouvService;


public class MedicamentGouvServiceImpl implements MedicamentGouvService {

	private RestHighLevelClient client;
	ObjectMapper mapper;
	private MedicamentGouvRepositoryMongo mongoRepository;
	private MedicamentGouvRepositoryElastic elasticRepository;

	public MedicamentGouvServiceImpl(ObjectMapper mapper, MedicamentGouvRepositoryMongo mongoRepository,
			MedicamentGouvRepositoryElastic elasticRepository, RestHighLevelClient client)
			throws IllegalArgumentException, IOException {
		this.mapper = mapper;
		this.mongoRepository = mongoRepository;
		this.elasticRepository = elasticRepository;
		this.client = client;
	}

	@Override
	public List<MedicamentGouvMongo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MedicamentGouvMongo findById(String id) {
		return mongoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public List<String> findByDenomination(String denomination) {

		System.out.println("CE SOIR NOUS DINNONS EN ENFER");

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

		return listeReturned;
	}
	
	public void initElasticIndex() throws IOException {
		if (checkIfIndexExists()) {
			List<MedicamentGouvMongo> liste = mongoRepository.findAll();
			System.out.println(liste.get(1560));
			for (MedicamentGouvMongo medicamentGouvMongo : liste) {
				System.out.println(medicamentGouvMongo.getDenomination());
				MedicamentGouvElastic medicamentGouvElastic = new MedicamentGouvElastic(medicamentGouvMongo);
				elasticRepository.save(medicamentGouvElastic);
			}
		}
	}

	private Boolean checkIfIndexExists() throws IOException {

		var resp = client.getLowLevelClient().performRequest(new Request("GET", "medicgouv/_stats"));
		var body = mapper.readTree(resp.getEntity().getContent());
		var size = body.get("indices").get("medicgouv").get("primaries").get("store").get("size_in_bytes");

		return size.asInt() <= 208;
	}

}
