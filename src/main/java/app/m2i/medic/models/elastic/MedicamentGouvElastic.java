package app.m2i.medic.models.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "medicgouv")
public class MedicamentGouvElastic {
	
	@Id
	private String id;
	
    private Integer codeCIS;
    private String denomination;
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


}
