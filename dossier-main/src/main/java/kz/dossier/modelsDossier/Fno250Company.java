package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fno250_company", schema = "imp_rn")
public class Fno250Company {
    @Id
    private Long id;

    private String iin;
    private String name_company;
    private String dolya;
    private String code_country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getName_company() {
        return name_company;
    }

    public void setName_company(String name_company) {
        this.name_company = name_company;
    }

    public String getDolya() {
        return dolya;
    }

    public void setDolya(String dolya) {
        this.dolya = dolya;
    }

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }
}
