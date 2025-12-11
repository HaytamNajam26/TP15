package ma.projet.graph.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeStats {
    private Integer count;
    private Double sum;
    private Double average;
}

