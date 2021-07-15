package app.m2i.medic.recherche.repositories.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.m2i.medic.recherche.models.mongo.MedicamentGouvMongo;

public interface MedicamentGouvRepositoryMongo extends MongoRepository<MedicamentGouvMongo, String>{
	
	List<MedicamentGouvMongo> findAllById(Iterable<String> ids);

}
