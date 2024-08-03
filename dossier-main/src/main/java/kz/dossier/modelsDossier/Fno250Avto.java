package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fno250_avto", schema = "imp_rn")
public class Fno250Avto {
    @Id
    private Long id;

    private String iin;
    private String marka;
    private String vin_code;
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

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }
}
