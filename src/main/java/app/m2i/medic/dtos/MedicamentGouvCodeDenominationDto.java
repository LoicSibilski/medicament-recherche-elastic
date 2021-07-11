package app.m2i.medic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentGouvCodeDenominationDto {
	
	private String codeCIS;
	
	private String denomination;

}
