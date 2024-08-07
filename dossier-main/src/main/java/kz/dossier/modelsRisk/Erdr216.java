package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "imp_risk.erdr216")

public class Erdr216 {
    private String bin;
    private String erdr;
    @Column(name = "code_state")
    private String codeState;
    @Column(name = "body_reg")
    private String bodyReg;
    @Column(name = "reg_dt")
    private String regDt;
    @Id
    private Long id;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getErdr() {
        return erdr;
    }

    public void setErdr(String erdr) {
        this.erdr = erdr;
    }

    public String getCodeState() {
        return codeState;
    }

    public void setCodeState(String codeState) {
        this.codeState = codeState;
    }

    public String getBodyReg() {
        return bodyReg;
    }

    public void setBodyReg(String bodyReg) {
        this.bodyReg = bodyReg;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
