package kz.dossier.dto;

public class ULDto {
    private String bin;
    private String fullName;
    private String oked;
    private String status;
    private String regDate;
    private Double infoPercentage;
    private Double riskPercentage;
    private Boolean isResident;

    public Boolean getResident() {
        return isResident;
    }

    public void setResident(Boolean resident) {
        isResident = resident;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public Double getInfoPercentage() {
        return infoPercentage;
    }

    public void setInfoPercentage(Double infoPercentage) {
        this.infoPercentage = infoPercentage;
    }

    public Double getRiskPercentage() {
        return riskPercentage;
    }

    public void setRiskPercentage(Double riskPercentage) {
        this.riskPercentage = riskPercentage;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOked() {
        return oked;
    }

    public void setOked(String oked) {
        this.oked = oked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
