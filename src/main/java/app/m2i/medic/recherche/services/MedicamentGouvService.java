package app.m2i.medic.recherche.services;

import java.io.IOException;
import java.util.List;

public interface MedicamentGouvService {
	
	public List<String> findByDenomination(String denomination);

	public void initElasticIndex() throws IOException;
}
