package app.m2i.medic.repositories.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import app.m2i.medic.models.elastic.MedicamentGouvElastic;

public interface MedicamentGouvRepositoryElastic extends ElasticsearchRepository<MedicamentGouvElastic, String >{

}
