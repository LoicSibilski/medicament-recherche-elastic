package app.m2i.medic.models.elastic;

import java.util.List;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCompletion {
	
    private String[] input;
    private Map<String, List<String>> contexts;
    private Integer weight;



    public CustomCompletion(String[] input) {
        this.input = input;
    }

    // Setter getter

}

