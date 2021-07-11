package app.m2i.medic.repositories.elastic;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import app.m2i.medic.models.elastic.MedicamentGouvElastic;

public interface MedicamentGouvRepositoryElastic extends ElasticsearchRepository<MedicamentGouvElastic, String >{
 
	public List<MedicamentGouvElastic> findByDenomination(String denomination);
	
	public List<MedicamentGouvElastic> findAll();

    //List<MedicamentGouvElastic> search(QueryBuilder query);

}
