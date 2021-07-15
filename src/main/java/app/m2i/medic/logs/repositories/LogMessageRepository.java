package app.m2i.medic.logs.repositories;


import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import app.m2i.medic.logs.documents.LogMessage;


public interface LogMessageRepository extends ElasticsearchRepository<LogMessage, String> {

	public List<LogMessage> findAll();
	public List<LogMessage> findAllByLevel(String level);
	public List<LogMessage> findAllByWho(String who);
}
