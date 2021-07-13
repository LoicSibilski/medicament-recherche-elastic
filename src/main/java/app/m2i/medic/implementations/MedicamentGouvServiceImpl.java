package app.m2i.medic.implementations;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
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
			MedicamentGouvRepositoryElastic elasticRepository, RestHighLevelClient client) throws IllegalArgumentException, IOException {
		this.mapper = mapper;
		this.mongoRepository = mongoRepository;
		this.elasticRepository = elasticRepository;
		this.client = client;
		this.initElasticIndex();
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
	public List<MedicamentGouvMongo> findByDenomination(String denomination) {

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
		
		System.out.println("oui");

		try {
			System.out.println("try avant");
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			System.out.println("try apres");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("try apres apres");

		Suggest suggest = searchResponse.getSuggest();
		System.out.println("ouafffffff");

		CompletionSuggestion entries = suggest.getSuggestion("suggestio-denomi");
		System.out.println("wooooooooooof");

		for (CompletionSuggestion.Entry entry : entries) {
			System.out.println(entry.getLength());
			System.out.println(entry.getOptions());
			System.out.println(entry.getText());
			System.out.println(entry.getOffset());
			System.out.println(entry.getClass());
			for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
				System.out.println(option.toString());
				String suggestText = option.getText().string();
				System.out.println(suggestText);
			}
		}

		System.out.println("ouaf" + denomination);
		List<MedicamentGouvElastic> medicsElastic = elasticRepository.findByDenomination(denomination);
		System.out.println("elastic size ===> " + medicsElastic.size());
		List<String> ids = medicsElastic.stream().map(medic -> medic.getId()).collect(Collectors.toList());
		System.out.println("String ids size ===> " + ids.size());
		return mongoRepository.findAllById(ids);
	}

	private void initElasticIndex() throws IllegalArgumentException, IOException {
//		if (checkElasticAlreadyInitialized()) {
			System.out.println("moncul");
			CreateIndexRequest createIndexRequest = new CreateIndexRequest("medicgouv").settings(Settings.builder().put("index.number_of_shards", 1)
					.put("index.number_of_replicas", 0).put("index.max_result_window", 16000));
			System.out.println("ouaf");
//			CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
			System.out.println("kmais");
			
			List<MedicamentGouvMongo> liste = mongoRepository.findAll();
			System.out.println(liste.get(1560));
			for (int i=0; i <300; i++) {
				
				String serialisedJson = mapper.writeValueAsString(liste.get(i));
				System.out.println(liste.get(i).getDenomination());
				MedicamentGouvElastic medicamentGouvElastic = mapper.readValue(serialisedJson, MedicamentGouvElastic.class);
				elasticRepository.save(medicamentGouvElastic);
//			}
		}
	}

	private Boolean checkElasticAlreadyInitialized() throws IOException {
		
		GetIndexRequest indexRequest = new GetIndexRequest().indices("medicgouv");
		return client.indices().exists(indexRequest, RequestOptions.DEFAULT);		
//		List<MedicamentGouvElastic> medicsElastic = elasticRepository.findAll();
//		return medicsElastic.isEmpty();
	}

}
