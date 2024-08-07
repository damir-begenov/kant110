package kz.dossier.modelsRisk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "imp_risk.verification_filed")
public class VerificationFiled {
    @Id
    private Long id;
    @Column(name = "reg_number")
    private String regNumber;
    @Column(name = "reg_date")
    private Date regDate;
    @Column(name = "refusal_date")
    private String refusalDate;
    @Column(name = "verification_num")
    private String verificationNum;
    @Column(name = "action_date")
    private Date actionDate;
    @Column(name = "bin")
    private String bin;
    @Column(name = "verification_type")
    private String verificationType;
    @Column(name = "basis_of_verification")
    private String basisOfVerification;
    @Column(name = "verification_period")
    private String verificationPeriod;
    @Column(name = "scope_of_control")
    private String scopeOfControl;
    @Column(name = "verification_result")
    private String verificationResult;
    @Column(name = "taken_measures")
    private String takenMeasures;
    @Column(name = "scope_of_verification")
    private String scopOfVerification;
    @Column(name = "state")
    private String state;
    @Column(name = "verification_organ")
    private String verificationOrgan;
    @Column(name = "moratorium_basis")
    private String moratoriumBasis;

    public String getVerificationPeriod() {
        return verificationPeriod;
    }

    public void setVerificationPeriod(String verificationPeriod) {
        this.verificationPeriod = verificationPeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getRefusalDate() {
        return refusalDate;
    }

    public void setRefusalDate(String refusalDate) {
        this.refusalDate = refusalDate;
    }

    public String getVerificationNum() {
        return verificationNum;
    }

    public void setVerificationNum(String verificationNum) {
        this.verificationNum = verificationNum;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public String getBasisOfVerification() {
        return basisOfVerification;
    }

    public void setBasisOfVerification(String basisOfVerification) {
        this.basisOfVerification = basisOfVerification;
    }

    public String getScopeOfControl() {
        return scopeOfControl;
    }

    public void setScopeOfControl(String scopeOfControl) {
        this.scopeOfControl = scopeOfControl;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public String getTakenMeasures() {
        return takenMeasures;
    }

    public void setTakenMeasures(String takenMeasures) {
        this.takenMeasures = takenMeasures;
    }

    public String getScopOfVerification() {
        return scopOfVerification;
    }

    public void setScopOfVerification(String scopOfVerification) {
        this.scopOfVerification = scopOfVerification;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVerificationOrgan() {
        return verificationOrgan;
    }

    public void setVerificationOrgan(String verificationOrgan) {
        this.verificationOrgan = verificationOrgan;
    }

    public String getMoratoriumBasis() {
        return moratoriumBasis;
    }

    public void setMoratoriumBasis(String moratoriumBasis) {
        this.moratoriumBasis = moratoriumBasis;
    }
}
