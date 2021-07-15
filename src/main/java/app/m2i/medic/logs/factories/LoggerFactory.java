package app.m2i.medic.logs.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.m2i.medic.logs.loggers.ElasticLogger;
import app.m2i.medic.logs.loggers.Logger;
import app.m2i.medic.logs.repositories.LogMessageRepository;

@Component
public class LoggerFactory {

	@Autowired
	private LogMessageRepository repository;
		
	public Logger getElasticLogger(String who) {
		return new ElasticLogger(who, repository);
	}
}
