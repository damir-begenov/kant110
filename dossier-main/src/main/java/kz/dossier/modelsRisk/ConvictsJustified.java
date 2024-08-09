package kz.dossier.modelsRisk;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "convicts_justified_new", schema = "imp_risk")
public class ConvictsJustified {
    @Nullable
    private String iin;
    @Nullable

    private String last_name;
    @Nullable

    private String first_name;
    @Nullable
    private String patronomyc;
    @Nullable
    private String birth_date;
    private String date_decision;
    private String prigovor;
    private String decision;
    private String nomer_erdr;
    private String name_of_organ;
    private String sud;
    private String code_statia;
    private String insert_date;
    @Id
    private Long id;

    public String getDate_decision() {
        return date_decision;
    }

    public void setDate_decision(String date_decision) {
        this.date_decision = date_decision;
    }

    public String getPrigovor() {
        return prigovor;
    }

    public void setPrigovor(String prigovor) {
        this.prigovor = prigovor;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getNomer_erdr() {
        return nomer_erdr;
    }

    public void setNomer_erdr(String nomer_erdr) {
        this.nomer_erdr = nomer_erdr;
    }

    public String getName_of_organ() {
        return name_of_organ;
    }

    public void setName_of_organ(String name_of_organ) {
        this.name_of_organ = name_of_organ;
    }

    public String getSud() {
        return sud;
    }

    public void setSud(String sud) {
        this.sud = sud;
    }

    public String getCode_statia() {
        return code_statia;
    }

    public void setCode_statia(String code_statia) {
        this.code_statia = code_statia;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    @Nullable
    public String getIin() {
        return iin;
    }

    public void setIin(@Nullable String iin) {
        this.iin = iin;
    }

    @Nullable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(@Nullable String last_name) {
        this.last_name = last_name;
    }

    @Nullable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(@Nullable String first_name) {
        this.first_name = first_name;
    }

    @Nullable
    public String getPatronomyc() {
        return patronomyc;
    }

    public void setPatronomyc(@Nullable String patronomyc) {
        this.patronomyc = patronomyc;
    }

    @Nullable
    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(@Nullable String birth_date) {
        this.birth_date = birth_date;
    }




    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }
}
