package app.m2i.medic.services.medicamentGouv;

import java.util.List;
import java.util.Optional;

import app.m2i.medic.models.mongo.MedicamentGouvMongo;

public interface MedicamentGouvService {

	public Optional<MedicamentGouvMongo> findById(String id);

	public List<MedicamentGouvMongo> findAll();

}
