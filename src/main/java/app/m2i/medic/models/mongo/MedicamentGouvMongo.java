package app.m2i.medic.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MedicamentGouvMongo {
	
	@Id
	private String id;
	
    private String codeCIS;
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
