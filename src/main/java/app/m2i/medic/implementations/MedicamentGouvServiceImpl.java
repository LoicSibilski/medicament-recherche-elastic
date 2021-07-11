package app.m2i.medic.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.m2i.medic.models.elastic.MedicamentGouvElastic;
import app.m2i.medic.models.mongo.MedicamentGouvMongo;
import app.m2i.medic.repositories.elastic.MedicamentGouvRepositoryElastic;
import app.m2i.medic.repositories.mongo.MedicamentGouvRepositoryMongo;
import app.m2i.medic.services.medicamentGouv.MedicamentGouvService;

public class MedicamentGouvServiceImpl implements MedicamentGouvService{
	
	ObjectMapper mapper;
	
	private MedicamentGouvRepositoryMongo mongoRepository;

	private MedicamentGouvRepositoryElastic elasticRepository;
	
	
	public MedicamentGouvServiceImpl(ObjectMapper mapper, MedicamentGouvRepositoryMongo mongoRepository,
			MedicamentGouvRepositoryElastic elasticRepository) {
		this.mapper = mapper;
		this.mongoRepository = mongoRepository;
		this.elasticRepository = elasticRepository;
		this.initElasticIndex();
	}
	
	
	@Override
	public List<MedicamentGouvMongo> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<MedicamentGouvMongo> findByName(String denomination){
		List<MedicamentGouvElastic> medicsElastic = elasticRepository.findByDenomination(denomination);
		List<String> ids = medicsElastic.stream()
				.map(medic->medic.getId())
				.collect(Collectors.toList());
		return mongoRepository.findAllById(ids);
	}
	
	@Override
	public Optional<MedicamentGouvMongo> findById(String id) {
		return mongoRepository.findById(id);
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
