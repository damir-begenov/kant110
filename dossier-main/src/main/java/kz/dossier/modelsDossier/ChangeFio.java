package kz.dossier.modelsDossier;

import io.micrometer.core.annotation.Counted;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "change_fio", schema = "imp_zags")
public class ChangeFio {
    private String iin;

    @Column(name = "SOURCE_INFO")
    private String source;
    @Id

    private String number_akt;

    private String surname_before;
    private String name_before;
    private String secondname_before;
    private String surname;
    private String name;
    private String secondname;

    private String to_date;
    private String remarks;

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNumber_akt() {
        return number_akt;
    }

    public void setNumber_akt(String number_akt) {
        this.number_akt = number_akt;
    }

    public String getSurname_before() {
        return surname_before;
    }

    public void setSurname_before(String surname_before) {
        this.surname_before = surname_before;
    }

    public String getName_before() {
        return name_before;
    }

    public void setName_before(String name_before) {
        this.name_before = name_before;
    }

    public String getSecondname_before() {
        return secondname_before;
    }

    public void setSecondname_before(String secondname_before) {
        this.secondname_before = secondname_before;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
