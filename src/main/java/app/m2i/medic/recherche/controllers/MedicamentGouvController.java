package app.m2i.medic.recherche.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.m2i.medic.logs.factories.LoggerFactory;
import app.m2i.medic.logs.loggers.Logger;
import app.m2i.medic.recherche.services.MedicamentGouvService;

@RestController
@RequestMapping("medicament/recherche")
@CrossOrigin
public class MedicamentGouvController {

	private final Logger LOGGER;

	@Autowired
	private MedicamentGouvService medicService;
	
	public MedicamentGouvController(LoggerFactory factory) {
		super();
		this.LOGGER = factory.getElasticLogger(MedicamentGouvController.class.getName());
	}

	@GetMapping("/denomination/{denomination}")
	public List<String> findByDenomination(@PathVariable String denomination) {
		LOGGER.info("Recherche d'un médicament avec la suggestion :  " + denomination);
		try {
			this.medicService.initElasticIndex();
		} catch (IOException e) {
			LOGGER.warn("Erreur dans l'initialisation de l'index ElasticSearch");
			e.printStackTrace();
		}
		return medicService.findByDenomination(denomination);
	}
	
}
