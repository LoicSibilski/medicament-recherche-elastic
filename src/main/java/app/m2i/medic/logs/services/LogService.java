package app.m2i.medic.logs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.m2i.medic.logs.documents.LogMessage;
import app.m2i.medic.logs.repositories.LogMessageRepository;

@Service
public class LogService {

	@Autowired
	private LogMessageRepository repository;
	
	public Iterable<LogMessage> findAll(){
		return this.repository.findAll();
	}

	public List<LogMessage> findAllByLevel(String level) {
		return repository.findAllByLevel(level);
	}

	public List<LogMessage> findAllByWho(String who) {
		return repository.findAllByWho(who);
	}
	
}
