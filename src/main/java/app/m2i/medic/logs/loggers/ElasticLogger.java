package app.m2i.medic.logs.loggers;

import java.time.LocalDateTime;

import app.m2i.medic.logs.documents.LogMessage;
import app.m2i.medic.logs.repositories.LogMessageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElasticLogger extends Logger{

	private String who;
	private LogMessageRepository repository;

	@Override
	public void log(String level, String who, String message) {
		LogMessage log = new LogMessage();
		log.setDate(LocalDateTime.now());
		log.setLevel(level);
		log.setMessage(message);
		log.setWho(who);
		this.repository.save(log);
	}
}