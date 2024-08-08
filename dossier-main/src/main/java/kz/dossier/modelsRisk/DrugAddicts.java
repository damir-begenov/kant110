package kz.dossier.modelsRisk;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drug_addicts_pfr", schema = "imp_risk")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrugAddicts {
    private Integer id;
    private String source;
    private String fio;
    @Id
    private String iin;
    private String ishod;
}
