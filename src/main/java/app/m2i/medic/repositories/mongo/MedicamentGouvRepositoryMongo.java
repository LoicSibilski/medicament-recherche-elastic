package app.m2i.medic.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.m2i.medic.models.mongo.MedicamentGouvMongo;

public interface MedicamentGouvRepositoryMongo extends MongoRepository<MedicamentGouvMongo, String>{

}
