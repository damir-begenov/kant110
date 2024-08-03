package kz.dossier.modelsDossier;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fno250_deposit", schema = "imp_rn")
public class Fno250Deposit {
    @Id
    private Long id;

    private String iin;
    private String bank;
    private String currency;
    private String code_country;
    private String summa;

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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    public String getSumma() {
        return summa;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }
}
