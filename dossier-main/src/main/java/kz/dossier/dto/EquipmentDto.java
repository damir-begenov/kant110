package kz.dossier.dto;

public class EquipmentDto {
    private String ownerIinBin;
    private String ownerName;
    private String proprietorIinBin;
    private String proprietorName;
    private String govNumber;
    private String regSeriesNum;
    private String regDate;
    private String regReason;
    private String endDate;
    private String endReason;
    private String vin;
    private String engineNum;
    private String brand;
    private String equipmentModel;
    private String manufacturer;
    private Integer issueYear;
    private Boolean isPledge;
    private Boolean isArrest;
    private Boolean isFirstReg;

    public String getOwnerIinBin() {
        return ownerIinBin;
    }

    public void setOwnerIinBin(String ownerIinBin) {
        this.ownerIinBin = ownerIinBin;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProprietorIinBin() {
        return proprietorIinBin;
    }

    public void setProprietorIinBin(String proprietorIinBin) {
        this.proprietorIinBin = proprietorIinBin;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getGovNumber() {
        return govNumber;
    }

    public void setGovNumber(String govNumber) {
        this.govNumber = govNumber;
    }

    public String getRegSeriesNum() {
        return regSeriesNum;
    }

    public void setRegSeriesNum(String regSeriesNum) {
        this.regSeriesNum = regSeriesNum;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegReason() {
        return regReason;
    }

    public void setRegReason(String regReason) {
        this.regReason = regReason;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getIssueYear() {
        return issueYear;
    }

    public void setIssueYear(Integer issueYear) {
        this.issueYear = issueYear;
    }

    public Boolean getPledge() {
        return isPledge;
    }

    public void setPledge(Boolean pledge) {
        isPledge = pledge;
    }

    public Boolean getArrest() {
        return isArrest;
    }

    public void setArrest(Boolean arrest) {
        isArrest = arrest;
    }

    public Boolean getFirstReg() {
        return isFirstReg;
    }

    public void setFirstReg(Boolean firstReg) {
        isFirstReg = firstReg;
    }
}
