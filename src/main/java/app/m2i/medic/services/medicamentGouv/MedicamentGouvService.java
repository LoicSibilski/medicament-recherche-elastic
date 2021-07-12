package app.m2i.medic.services.medicamentGouv;

import java.util.List;
import java.util.Optional;

import app.m2i.medic.models.mongo.MedicamentGouvMongo;

public interface MedicamentGouvService {
	
	public List<MedicamentGouvMongo> findAll();
	
	public MedicamentGouvMongo findById(String id);

	public List<MedicamentGouvMongo> findByDenomination(String denomination);

}
