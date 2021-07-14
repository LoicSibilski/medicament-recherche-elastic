package app.m2i.medic.services.medicamentGouv;

import java.util.List;

import app.m2i.medic.models.mongo.MedicamentGouvMongo;

public interface MedicamentGouvService {
	
	public List<MedicamentGouvMongo> findAll();
	
	public MedicamentGouvMongo findById(String id);

	public List<String> findByDenomination(String denomination);

}
