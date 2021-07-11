package app.m2i.medic.implementations;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexMetadata;
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
			MedicamentGouvRepositoryElastic elasticRepository, RestHighLevelClient client) {
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

		CompletionSuggestionBuilder  termSuggestionBuilder = SuggestBuilders.completionSuggestion("denomination")
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

		for (CompletionSuggestion.Entry entry : entries) {
			for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
				String suggestText = option.getText().string();
				System.out.println(suggestText);
			}
		}

//		SuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("denomination").text(denomination);
//		SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion("suggestion_denomination_medic", termSuggestionBuilder);
//		
//		//SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().from(0).size(100).suggest(suggestBuilder);
//		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//		BoolQueryBuilder qb = QueryBuilders.boolQuery();
//		PrefixQueryBuilder namePQBuilder = QueryBuilders.prefixQuery("denomination", denomination);
//        qb.should(namePQBuilder);
//        sourceBuilder.query(qb);
//
//		
//		SearchRequest searchRequest = new SearchRequest().indices("medicgouv").source(sourceBuilder);
//        System.out.println("Search JSON query \n" + searchRequest.source().toString()); //Generated ES search JSON.
//
//		SearchResponse searchResponse;
//		try {
//			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//			System.out.println("searchResponse => " + searchResponse.toString());
//			RestStatus status = searchResponse.status();
//			System.out.println("Status TRY REST => "  + status);
//			SearchHits hits = searchResponse.getHits();
//			System.out.println("hits " + hits.getMaxScore());
//			SearchHit[] searchHits = hits.getHits();
//			System.out.println(searchHits.length);
//			for (SearchHit hit : searchHits) {
//			    System.out.println(hit.toString());
//			}

//			Suggest suggest = searchResponse.getSuggest(); 
//			System.out.println(suggest.toString());
//			TermSuggestion termSuggestion = suggest.getSuggestion("suggestion_denomination_medic"); 
//			for (TermSuggestion.Entry entry : termSuggestion.getEntries()) { 
//				System.out.println(entry.toString());
//			    for (TermSuggestion.Entry.Option option : entry) {
//			    	System.out.println("oooo=> +" + option.toString());
//			        String suggestText = option.getText().string();
//			        System.out.println("Suggest Text =>> " + suggestText);
//			    }
//			}

//			
//		} catch (IOException e) {
//			System.out.println("Imple => FindByDenomination =>  searResponse FAILED");
//			e.printStackTrace();
//		}

//		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
//				.must(QueryBuilders.matchQuery("denomination", denomination));
//		SuggestionBuilder suggestionBuilder = new TermSuggestionBuilder("denomination").text(denomination);
//		SuggestBuilder suggestion = new SuggestBuilder().addSuggestion("my-suggest-1", suggestionBuilder);
//
//		SearchRequestBuilder builder = client.search("sample").setTypes("medicgouv").setQuery(boolQuery)
//				.suggest(suggestion);
//
//		SearchResponse searchResponse;
//		try {
//			searchResponse = builder.execute().get();
//
//			for (SearchHit hit : searchResponse.getHits().getHits()) {
//			    System.out.println(hit.getSourceAsString());
//				//LOG.info("Result: " + hit.getSourceAsString());
//			}
//		} catch (InterruptedException | ExecutionException e) {
//			System.out.println("cath ma grosse queue");
////			    LOG.error("Exception while executing query {}", e);
//		}
//		
//		SearchRequest request = new SearchRequest("medicgouv");
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(boolQuery);
//		searchSourceBuilder.sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
//		request.source(searchSourceBuilder);
//
//		SearchResponse scrollResp = client.search(sreq);

		System.out.println("ouaf" + denomination);
		List<MedicamentGouvElastic> medicsElastic = elasticRepository.findByDenomination(denomination);
		System.out.println("elastic size ===> " + medicsElastic.size());
		List<String> ids = medicsElastic.stream().map(medic -> medic.getId()).collect(Collectors.toList());
		System.out.println("String ids size ===> " + ids.size());
		return mongoRepository.findAllById(ids);
	}

	private void initElasticIndex() {
		if (checkElasticAlreadyInitialized()) {
			List<MedicamentGouvMongo> liste = mongoRepository.findAll();
			System.out.println(liste.get(1560));
			for (MedicamentGouvMongo medic : liste) {
				elasticRepository.save(mapper.convertValue(medic, MedicamentGouvElastic.class));
			}
		}
	}

	private Boolean checkElasticAlreadyInitialized() {
		List<MedicamentGouvElastic> medicsElastic = elasticRepository.findAll();
		return medicsElastic.isEmpty();
	}

}
