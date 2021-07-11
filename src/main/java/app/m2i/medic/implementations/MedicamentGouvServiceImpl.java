package app.m2i.medic.implementations;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	}
	
	public void save () {
		mongoRepository.save(new MedicamentGouvMongo());
	}

	public List<MedicamentGouvMongo> findAll() {
		System.out.println("yes paapa");
		List<MedicamentGouvMongo> liste = mongoRepository.findAll();
		System.out.println(liste.size());
		System.out.println(liste.get(152).toString());
		return mongoRepository.findAll();
	}

	public Optional<MedicamentGouvMongo> findById(String id) {
		return mongoRepository.findById(id);
	}

}
