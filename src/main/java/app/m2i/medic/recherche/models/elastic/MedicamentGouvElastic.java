package app.m2i.medic.recherche.models.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.core.completion.Completion;

import app.m2i.medic.recherche.models.mongo.MedicamentGouvMongo;

import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "medicgouv")
public class MedicamentGouvElastic {

	@Id
	private String id;

	private Integer codeCIS;

	@CompletionField(maxInputLength = 100)
	private Completion denomination;
	private String formePharmaceutique;
	private String voiesAdministration;
	private String statutAdministratif;
	private String typeProcedureAutorisation;
	private String etatCommercialisation;
	private String dateAMM;
	private String statutBdm;
	private String numeroAutorisationEuropeenne;
	private String titulaires;
	private String surveillanceRenforcee;

	public MedicamentGouvElastic(MedicamentGouvMongo medicamentGouvMongo) {
		String[] tab = { medicamentGouvMongo.getDenomination() };
		Completion comp = new Completion(tab);
		this.codeCIS = medicamentGouvMongo.getCodeCIS();
		this.denomination = comp;
		this.dateAMM = medicamentGouvMongo.getDateAMM();
		this.etatCommercialisation = medicamentGouvMongo.getEtatCommercialisation();
		this.formePharmaceutique = medicamentGouvMongo.getFormePharmaceutique();
		this.numeroAutorisationEuropeenne = medicamentGouvMongo.getNumeroAutorisationEuropeenne();
		this.statutAdministratif = medicamentGouvMongo.getStatutAdministratif();
		this.statutBdm = medicamentGouvMongo.getStatutBdm();
		this.surveillanceRenforcee = medicamentGouvMongo.getSurveillanceRenforcee();
		this.titulaires = medicamentGouvMongo.getTitulaires();
		this.typeProcedureAutorisation = medicamentGouvMongo.getTypeProcedureAutorisation();
		this.voiesAdministration =  medicamentGouvMongo.getVoiesAdministration();

	}

}
