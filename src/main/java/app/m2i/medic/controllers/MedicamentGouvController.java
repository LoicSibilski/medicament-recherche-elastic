package app.m2i.medic.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.m2i.medic.initdb.elastic.InitialisationElasticIndexes;
import app.m2i.medic.models.mongo.MedicamentGouvMongo;
import app.m2i.medic.services.medicamentGouv.MedicamentGouvService;

@RestController
@RequestMapping("medicament/recherche")
@CrossOrigin
public class MedicamentGouvController {

	@Autowired
	private MedicamentGouvService medicService;
	
	@Autowired
	private InitialisationElasticIndexes initElastic;

	@GetMapping("/id/{id}")
	public MedicamentGouvMongo findById(@PathVariable String id) {
		return medicService.findById(id);
	}

	@GetMapping("/denomination/{denomination}")
	public List<String> findByDenomination(@PathVariable String denomination) {
		try {
			this.initElastic.initElasticIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return medicService.findByDenomination(denomination);
	}


	@GetMapping("")
	public List<MedicamentGouvMongo> findAll() {
		return medicService.findAll();
	}
	
}
